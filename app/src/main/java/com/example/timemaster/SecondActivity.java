package com.example.timemaster;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class SecondActivity extends AppCompatActivity {

    private ListView listView;
    private List<String> itemList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2);

        listView = findViewById(R.id.listView);
        Button buttonPlus = findViewById(R.id.button_plus);
        Button buttonBack = findViewById(R.id.button_back);

        // Получаем переданную дату из Intent
        Intent intent = getIntent();
        String selectedDate = intent.getStringExtra("SELECTED_DATE");

        // Создаем список элементов
        itemList = new ArrayList<>();
        itemList.add("Selected Date: " + selectedDate);
        itemList.add("Item 1");
        itemList.add("Item 2");
        itemList.add("Item 3");
        itemList.add("Item 4");
        itemList.add("Item 5");

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Получаем новое имя задачи из Intent
            String newItem = data.getStringExtra("NEW_ITEM");
            if (newItem != null) {
                // Добавляем новую задачу в список
                itemList.add(newItem);
                // Уведомляем адаптер об изменениях
                adapter.notifyDataSetChanged();
            }
        }
    }}
