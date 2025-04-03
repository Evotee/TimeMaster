package com.example.timemaster;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class SecondActivity extends AppCompatActivity {

    private ListView listView;
    private List<String> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2);

        listView = findViewById(R.id.listView);

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
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, itemList);
        listView.setAdapter(adapter);
    }
}
