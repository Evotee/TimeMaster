package com.example.timemaster;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

public class ThirdActivity extends AppCompatActivity {

    private CheckBox checkBox1, checkBox2, checkBox3, checkBoxAllDay; // Чекбоксы
    private EditText editText; // Поле ввода
    private Button buttonSave, buttonBack; // Кнопки

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity3);
        createNotificationChannel();

        // Инициализация элементов интерфейса
        checkBox1 = findViewById(R.id.checkBox1);
        checkBox2 = findViewById(R.id.checkBox2);
        checkBox3 = findViewById(R.id.checkBox3);
        checkBoxAllDay = findViewById(R.id.checkBox_all_day);
        editText = findViewById(R.id.editText);
        buttonSave = findViewById(R.id.button_save);
        buttonBack = findViewById(R.id.button_back);

        // Изначально скрываем чекбокс "Весь день"
        checkBoxAllDay.setVisibility(View.GONE);

        // Устанавливаем слушатели для чекбоксов
        checkBox2.setOnCheckedChangeListener((buttonView, isChecked) -> updateAllDayVisibility());
        checkBox1.setOnCheckedChangeListener((buttonView, isChecked) -> updateAllDayVisibility());
        checkBox3.setOnCheckedChangeListener((buttonView, isChecked) -> updateAllDayVisibility());

        // Обработчик нажатия кнопки "Сохранить"
        buttonSave.setOnClickListener(v -> {
            String newItem = editText.getText().toString();
            if (!newItem.isEmpty()) {
                // Проверяем, отмечен ли хотя бы один чекбокс, и не отмечен ли чекбокс "Весь день"
                if ((checkBox1.isChecked() || checkBox2.isChecked() || checkBox3.isChecked()) && !checkBoxAllDay.isChecked()) {
                    // Открываем MaterialTimePicker, если хотя бы один чекбокс отмечен и "Весь день" не отмечен
                    openTimePicker(newItem);
                } else {
                    // Если ни один чекбокс не отмечен или "Весь день" отмечен, просто возвращаем результат
                    returnResult(newItem);
                }
            } else {
                Toast.makeText(ThirdActivity.this, "Пожалуйста, введите название", Toast.LENGTH_SHORT).show();
            }
        });

        // Обработчик нажатия кнопки "Назад"
        buttonBack.setOnClickListener(v -> finish()); // Закрываем ThirdActivity и возвращаемся в SecondActivity
    }

    private void createNotificationChannel() {
        NotificationChannel channel = new NotificationChannel(
                "task_notifications",
                "Task Notifications",
                NotificationManager.IMPORTANCE_HIGH
        );
        NotificationManager manager = getSystemService(NotificationManager.class);
        if (manager != null) {
            manager.createNotificationChannel(channel);
        }

    }


    private void updateAllDayVisibility() {
        // Проверяем, отмечен ли чекбокс "Напоминание" или любой из других чекбоксов
        boolean isReminderChecked = checkBox2.isChecked();
        boolean isAnyOtherChecked = checkBox1.isChecked() || checkBox3.isChecked();

        // Устанавливаем видимость чекбокса "Весь день"
        checkBoxAllDay.setVisibility(isReminderChecked && !isAnyOtherChecked ? View.VISIBLE : View.GONE);

        // Если "Напоминание" не отмечено, сбрасываем "Весь день"
        if (!isReminderChecked) {
            checkBoxAllDay.setChecked(false);
        }
    }

    private void openTimePicker(final String newItem) {
        MaterialTimePicker picker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(12)
                .setMinute(0)
                .setTitleText(getString(R.string.time_picker_title)) // Используем строку из ресурсов
                .setPositiveButtonText(getString(R.string.time_picker_ok)) // Кнопка "ОК"
                .setNegativeButtonText(getString(R.string.time_picker_cancel)) // Кнопка "Отмена"
                .build();

        picker.show(getSupportFragmentManager(), "timePicker");

        picker.addOnPositiveButtonClickListener(v -> {
            int hour = picker.getHour();
            int minute = picker.getMinute();
            // Обработка
            // Обработка выбранного времени
            returnResultWithTime(newItem, hour, minute); // Передаем выбранное время
        });
    }

    private void returnResult(String newItem) {
        // Создаем Intent для передачи данных обратно
        Intent resultIntent = new Intent();
        resultIntent.putExtra("NEW_ITEM", newItem);
        setResult(RESULT_OK, resultIntent);
        finish(); // Закрываем ThirdActivity и возвращаемся в SecondActivity
    }

    private void returnResultWithTime(String newItem, int hour, int minute) {
        // Создаем Intent для передачи данных обратно
        Intent resultIntent = new Intent();
        resultIntent.putExtra("NEW_ITEM", newItem);
        resultIntent.putExtra("HOUR", hour);
        resultIntent.putExtra("MINUTE", minute);
        setResult(RESULT_OK, resultIntent);

        // Устанавливаем AlarmManager для уведомления
        scheduleNotification(newItem); // Передаем текст из EditText

        finish(); // Закрываем ThirdActivity и возвращаемся в SecondActivity
    }

    private void scheduleNotification(String message) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificationReceiver.class);
        intent.putExtra("MESSAGE", message); // Передаем текст уведомления

        // Указываем флаг FLAG_IMMUTABLE
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Устанавливаем уведомление через 10 секунд для тестирования
        long triggerAtMillis = System.currentTimeMillis() + 10000; // 10 секунд

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
        Log.d("ThirdActivity", "Notification scheduled for: " + triggerAtMillis);
    }
}
