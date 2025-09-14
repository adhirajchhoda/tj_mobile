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

        // Listener for Level 1 button to start Lab01Activity
        level1Button.setOnClickListener(v -> {
            Intent intent = new Intent(MainMenuActivity.this, Lab01Activity.class);
            startActivity(intent);
            // Optionally, show a toast or log that Lab01Activity is being started
            // Toast.makeText(MainMenuActivity.this, "Loading Lab 01...", Toast.LENGTH_SHORT).show();
        });

        // Listener for Level 2 button to start Lab02Activity
        level2Button.setOnClickListener(v -> {
            Intent intent = new Intent(MainMenuActivity.this, Lab02Activity.class);
            startActivity(intent);
            // Optionally, show a toast or log that Lab02Activity is being started
            // Toast.makeText(MainMenuActivity.this, "Loading Lab 02...", Toast.LENGTH_SHORT).show();
        });

        // Common listener for other level buttons (Levels 3-5)
        View.OnClickListener otherLevelsClickListener = v -> {
            Button b = (Button) v;
            Toast.makeText(MainMenuActivity.this, b.getText().toString() + " selected! (Not Lab 01 or Lab 02)", Toast.LENGTH_SHORT).show();
            // Implement navigation or other actions for these buttons as needed
        };

        level3Button.setOnClickListener(otherLevelsClickListener);
        level4Button.setOnClickListener(otherLevelsClickListener);
        level5Button.setOnClickListener(otherLevelsClickListener);

        backButton.setOnClickListener(v -> {
            // Intent intent = new Intent(MainMenuActivity.this, MainActivity.class);
            // startActivity(intent);
            finish(); // Simply finish to go back to the previous activity (MainActivity)
        });
    }
}
