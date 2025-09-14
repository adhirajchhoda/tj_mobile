package com.adhiraj.lab01;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton; // Added for ImageButton
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class Lab01Activity extends AppCompatActivity {
    ImageButton lab01BackButton; // Changed to ImageButton
    Button incrementButton;
    TextView countDisplay;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab01);

        lab01BackButton = findViewById(R.id.lab01_back_button); // Initialize ImageButton
        incrementButton = findViewById(R.id.increment_button);
        countDisplay = findViewById(R.id.count_textview);

        countDisplay.setText(String.valueOf(count));
        updateCountDisplayColor();

        lab01BackButton.setOnClickListener(v -> {
            finish(); // Go back to the previous activity
        });

        incrementButton.setOnClickListener(v -> {
            count++;
            System.out.println("incrementing to " + count);
            Log.i("testing: incrementing to", String.valueOf(count));
            countDisplay.setText(String.valueOf(count));
            updateCountDisplayColor();
        });
    }

    public void decrement(View view) {
        count--;
        Log.i("testing: decrementing to", String.valueOf(count));
        countDisplay.setText(String.valueOf(count));
        updateCountDisplayColor();
    }

    public void updateCountDisplayColor() {
        if (count < 0) {
            countDisplay.setTextColor(ContextCompat.getColor(this, R.color.nonchalant_red));
        } else {
            countDisplay.setTextColor(ContextCompat.getColor(this, R.color.black));
        }
    }
}
