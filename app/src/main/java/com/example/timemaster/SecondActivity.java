package com.example.timemaster;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

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

        // Получаем переданную дату из Intent
        Intent intent = getIntent();
        selectedDate = intent.getStringExtra("SELECTED_DATE");

        // Инициализируем DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Создаем список элементов и загружаем данные из базы данных
        itemList = new ArrayList<>();
        loadTasks(selectedDate);

        // Создаем адаптер и устанавливаем его для ListView
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, itemList);
        listView.setAdapter(adapter);

        // Устанавливаем обработчик нажатия на кнопку "плюс"
        buttonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondActivity.this, ThirdActivity.class);
                startActivityForResult(intent, 1); // Запускаем ThirdActivity с кодом запроса
            }
        });

        // Устанавливаем обработчик нажатия на кнопку "Назад"
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Закрывает SecondActivity и возвращает на предыдущую активность
            }
        });
    }

    private void loadTasks(String date) {
        // Загружаем задачи из базы данных для выбранной даты
        List<String> tasks = databaseHelper.getTasksForDate(date);
        itemList.addAll(tasks);
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
