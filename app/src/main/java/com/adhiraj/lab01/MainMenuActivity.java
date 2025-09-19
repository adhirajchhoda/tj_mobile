package com.adhiraj.lab01;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Button lab01Button = findViewById(R.id.level_1_button);
        Button lab02Button = findViewById(R.id.level_2_button);
        Button lab03Button = findViewById(R.id.level_3_button);
        Button lab04Button = findViewById(R.id.level_4_button);
        lab01Button.setOnClickListener(v -> {
            Intent intent = new Intent(MainMenuActivity.this, Lab01Activity.class);
            startActivity(intent);
        });

        lab02Button.setOnClickListener(v -> {
            Intent intent = new Intent(MainMenuActivity.this, Lab02Activity.class);
            startActivity(intent);
        });

        lab03Button.setOnClickListener(v -> {
            Intent intent = new Intent(MainMenuActivity.this, Lab03Activity.class);
            startActivity(intent);
        });

        lab04Button.setOnClickListener(v -> {
            Intent intent = new Intent(MainMenuActivity.this, Lab04Activity.class);
            startActivity(intent);
        });
    }
}
