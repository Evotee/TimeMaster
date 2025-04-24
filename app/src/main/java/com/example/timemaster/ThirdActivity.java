package com.example.timemaster;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

public class ThirdActivity extends AppCompatActivity {

    private CheckBox checkBox1, checkBox2, checkBox3, checkBoxAllDay; // Добавлен чекбокс для "Весь день"
    private EditText editText;
    private Button buttonSave, buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity3); // Убедитесь, что у вас есть activity3.xml

        // Инициализация чекбоксов
        checkBox1 = findViewById(R.id.checkBox1);
        checkBox2 = findViewById(R.id.checkBox2); // Чекбокс для напоминания
        checkBox3 = findViewById(R.id.checkBox3);
        checkBoxAllDay = findViewById(R.id.checkBox_all_day); // Инициализация чекбокса "Весь день"
        editText = findViewById(R.id.editText);
        buttonSave = findViewById(R.id.button_save);
        buttonBack = findViewById(R.id.button_back); // Инициализация кнопки "Назад"

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });

        // Обработчик нажатия кнопки "Назад"
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Закрываем ThirdActivity и возвращаемся в SecondActivity
            }
        });
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

        picker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = picker.getHour();
                int minute = picker.getMinute();
                // Обработка выбранного времени
                returnResult(newItem, hour, minute);
            }
        });
    }

    private void returnResult(String newItem) {
        // Создаем Intent для передачи данных обратно
        Intent resultIntent = new Intent();
        resultIntent.putExtra("NEW_ITEM", newItem);
        setResult(RESULT_OK, resultIntent);
        finish(); // Закрываем ThirdActivity и возвращаемся в SecondActivity
    }


        private void returnResult(String newItem, int hour, int minute) {
        // Создаем Intent для передачи данных обратно
        Intent resultIntent = new Intent();
        resultIntent.putExtra("NEW_ITEM", newItem);
        resultIntent.putExtra("HOUR", hour);
        resultIntent.putExtra("MINUTE", minute);
        setResult(RESULT_OK, resultIntent);
        finish(); // Закрываем ThirdActivity и возвращаемся в SecondActivity
    }
}
