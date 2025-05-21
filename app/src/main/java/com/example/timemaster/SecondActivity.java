package com.example.timemaster;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import java.util.Calendar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class SecondActivity extends AppCompatActivity {

    private ListView listView;
    private List<String> itemList;
    private ArrayAdapter<String> adapter;
    private DatabaseHelper databaseHelper;
    private String selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2);

        listView = findViewById(R.id.listView);
        Button buttonPlus = findViewById(R.id.button_plus);
        Button buttonBack = findViewById(R.id.button_back);
        ImageButton buttonClearAll = findViewById(R.id.button_clear_all);
        Button buttonCopyTasks = findViewById(R.id.button_copy_tasks);

        Intent intent = getIntent();
        selectedDate = intent.getStringExtra("SELECTED_DATE");
        String databasePath = intent.getStringExtra("DATABASE_NAME");

        databaseHelper = new DatabaseHelper(this, databasePath); // Используем новую базу данных
        itemList = new ArrayList<>();
        loadTasks(selectedDate);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, itemList);
        listView.setAdapter(adapter);

        buttonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondActivity.this, ThirdActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        buttonClearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearTasks();
            }
        });

        buttonCopyTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(); // Показываем диалог выбора даты
            }
        });
    }

    private void loadTasks(String date) {
        // Загружаем задачи из базы данных для выбранной даты
        List<String> tasks = databaseHelper.getTasksForDate(date);
        itemList.addAll(tasks);
    }

    private void clearTasks() {
        // Проверяем, есть ли задачи в списке
        if (itemList.isEmpty()) {
            Toast.makeText(this, "Задач еще нет", Toast.LENGTH_SHORT).show();
            return; // Выходим из метода, если задач нет
        }

        // Показываем диалоговое окно с подтверждением
        new AlertDialog.Builder(this)
                .setTitle("Подтверждение")
                .setMessage("Вы точно хотите удалить все задачи?")
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Удаляем все задачи из базы данных для выбранной даты
                        databaseHelper.clearTasksForDate(selectedDate);
                        // Очищаем список задач
                        itemList.clear();
                        // Уведомляем адаптер об изменениях
                        adapter.notifyDataSetChanged();
                        Toast.makeText(SecondActivity.this, "Все задачи удалены", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Закрываем диалоговое окно, ничего не делая
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert) // Добавьте иконку, если хотите
                .show();
    }

    private void showDatePicker() {
        // Получаем текущую дату
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Создаем диалог выбора даты
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {                        // Форматируем выбранную дату
                        String newDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay);
                        copyTasksToNewDate(newDate); // Копируем задачи на новую дату
                    }
                }, year, month, day);
        datePickerDialog.show(); // Показываем диалог выбора даты
    }

    private void copyTasksToNewDate(String newDate) {
        // Копируем задачи на новую дату в базе данных
        for (String task : itemList) {
            databaseHelper.addTask(newDate, task); // Сохраняем каждую задачу на новую дату
        }

        // Загружаем задачи для новой даты и обновляем список
        itemList.clear(); // Очищаем текущий список
        loadTasks(newDate); // Загружаем задачи для новой даты
        adapter.notifyDataSetChanged(); // Уведомляем адаптер об изменениях

        Toast.makeText(this, "Задачи скопированы на " + newDate, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Получаем новое имя задачи из Intent
            String newItem = data.getStringExtra("NEW_ITEM");
            if (newItem != null) {
                // Добавляем новую задачу в список и в базу данных
                itemList.add(newItem);
                databaseHelper.addTask(selectedDate, newItem); // Сохраняем задачу в базе данных
                // Уведомляем адаптер об изменениях
                adapter.notifyDataSetChanged();
                Toast.makeText(this, "Задача добавлена", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

