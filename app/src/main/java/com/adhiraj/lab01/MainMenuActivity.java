package com.adhiraj.lab01;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.view.View;

public class MainMenuActivity extends AppCompatActivity {

    Button level1Button;
    Button level2Button;
    Button level3Button;
    Button level4Button;
    Button level5Button;
    ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        level1Button = findViewById(R.id.level_1_button);
        level2Button = findViewById(R.id.level_2_button);
        level3Button = findViewById(R.id.level_3_button);
        level4Button = findViewById(R.id.level_4_button);
        level5Button = findViewById(R.id.level_5_button);
        backButton = findViewById(R.id.back_button);

        View.OnClickListener levelClickListener = v -> {
            Button b = (Button) v;
            Toast.makeText(MainMenuActivity.this, b.getText().toString() + " selected!", Toast.LENGTH_SHORT).show();
        };

        level1Button.setOnClickListener(levelClickListener);
        level2Button.setOnClickListener(levelClickListener);
        level3Button.setOnClickListener(levelClickListener);
        level4Button.setOnClickListener(levelClickListener);
        level5Button.setOnClickListener(levelClickListener);

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainMenuActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
