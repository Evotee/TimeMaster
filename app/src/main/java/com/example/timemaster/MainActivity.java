package com.example.timemaster;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CalendarView calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year,
                                            int month, int dayOfMonth) {
                // Форматируем выбранную дату
                String selectedDate = String.format("%02d-%02d-%d", month + 1, dayOfMonth, year);

                // Создаем Intent для перехода на SecondActivity
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                intent.putExtra("SELECTED_DATE", selectedDate); // Передаем выбранную дату

                // Запускаем SecondActivity
                startActivity(intent);

                // Показываем Toast с выбранной датой (по желанию)
                Toast.makeText(getApplicationContext(), selectedDate, Toast.LENGTH_LONG).show();
            }
        });
    }
}