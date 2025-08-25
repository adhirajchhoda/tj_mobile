package com.adhiraj.lab01;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {
    Button incrementButton;
    TextView greetingDisplay;
    TextView countDisplay;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        incrementButton = findViewById(R.id.increment_button);
        countDisplay = findViewById(R.id.count_textview);
        greetingDisplay = findViewById(R.id.greeting_textview);
        incrementButton.setOnClickListener(v -> {
            Log.i("testing: incrementing", String.valueOf(++count));
            countDisplay.setText(String.valueOf(count));
            updateCountDisplayColor();
        });
    }

    public void decrement(View view) {
        Log.i("testing: decrementing", String.valueOf(--count));
        updateCountDisplayColor();
        countDisplay.setText(String.valueOf(count));
    }

    public void updateCountDisplayColor() {
        if (count < 0) {
            countDisplay.setTextColor(ContextCompat.getColor(this, R.color.nonchalant_red));
        } else {
            countDisplay.setTextColor(ContextCompat.getColor(this, R.color.black));
        }
    }
}