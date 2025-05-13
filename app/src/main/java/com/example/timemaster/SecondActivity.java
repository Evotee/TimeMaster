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
                copyTasksToFirstActivity();
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

    private void copyTasksToFirstActivity() {
        // Здесь вы можете реализовать логику копирования задач в FirstActivity
        // Например, вы можете передать задачи в FirstActivity через Intent
        Intent intent = new Intent(SecondActivity.this, MainActivity.class);
        intent.putStringArrayListExtra("TASKS_LIST", new ArrayList<>(itemList));
        startActivity(intent);
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
