package com.example.timemaster;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ThirdActivity extends AppCompatActivity {

    private CheckBox checkBox1, checkBox2, checkBox3;
    private EditText editText;
    private Button buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity3); // Убедитесь, что у вас есть activity3.xml

        checkBox1 = findViewById(R.id.checkBox1);
        checkBox2 = findViewById(R.id.checkBox2);
        checkBox3 = findViewById(R.id.checkBox3);
        editText = findViewById(R.id.editText);
        buttonSave = findViewById(R.id.button_save);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newItem = editText.getText().toString();
                if (!newItem.isEmpty()) {
                    // Создаем Intent для передачи данных обратно
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("NEW_ITEM", newItem);
                    setResult(RESULT_OK, resultIntent);
                    finish(); // Закрываем ThirdActivity и возвращаемся в SecondActivity
                } else {
                    Toast.makeText(ThirdActivity.this, "Please enter a name", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
