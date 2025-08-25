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
    int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        incrementButton = findViewById(R.id.increment_button);
        greetingDisplay = findViewById(R.id.greeting_textview);
        incrementButton.setOnClickListener(v -> {
            System.out.println("incrementing" + ++count);
            Log.i("testing: incrementing", String.valueOf(++count));
            greetingDisplay.setText(String.valueOf(count));
        });


        TextView textView = findViewById(R.id.textView);
        textView.setTextColor(ContextCompat.getColor(this, R.color.nonchalant_red));
    }
    public void decrement(View view) {
        Log.i("testing: decrementing", String.valueOf(--count));
    }
}