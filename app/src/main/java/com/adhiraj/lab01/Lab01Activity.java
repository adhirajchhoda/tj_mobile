package com.adhiraj.lab01;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class Lab01Activity extends AppCompatActivity {
    ImageButton lab01BackButton;
    Button incrementButton;
    TextView countDisplay;
    int count = 0;

    private int[] layoutIds = {
            R.layout.layout_one,
            R.layout.layout_two,
            R.layout.layout_three,
            R.layout.layout_four,
            R.layout.layout_five,
            R.layout.layout_six,
            R.layout.layout_seven
    };
    private int currentLayoutIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab01);

        initializeViews();
        updateCountDisplayColor();

        if (lab01BackButton != null) {
            lab01BackButton.setOnClickListener(v -> finish());
        }

        if (incrementButton != null) {
            incrementButton.setOnClickListener(v -> {
                count++;
                System.out.println("incrementing to " + count);
                Log.i("testing: incrementing to", String.valueOf(count));
                if (countDisplay != null) {
                    countDisplay.setText(String.valueOf(count));
                }
                updateCountDisplayColor();
            });
        }
    }
    private void initializeViews() {
        lab01BackButton = findViewById(R.id.lab01_back_button);
        incrementButton = findViewById(R.id.increment_button);
        countDisplay = findViewById(R.id.count_textview);
        if (countDisplay != null) {
            countDisplay.setText(String.valueOf(count));
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            currentLayoutIndex++;
            currentLayoutIndex %= layoutIds.length;
            setContentView(layoutIds[currentLayoutIndex]);
            initializeViews();
            if (lab01BackButton != null) {
                lab01BackButton.setOnClickListener(v -> finish());
            }
            if (incrementButton != null) {
                incrementButton.setOnClickListener(v -> {
                    count++;
                    System.out.println("incrementing to " + count);
                    Log.i("testing: incrementing to", String.valueOf(count));
                    if (countDisplay != null) {
                        countDisplay.setText(String.valueOf(count));
                    }
                    updateCountDisplayColor();
                });
            }
            updateCountDisplayColor();
            return true;
        }
        return super.onTouchEvent(event);
    }

    public void decrement(View view) {
        count--;
        Log.i("testing: decrementing to", String.valueOf(count));
        if (countDisplay != null) {
            countDisplay.setText(String.valueOf(count));
        }
        updateCountDisplayColor();
    }

    public void updateCountDisplayColor() {
        if (countDisplay != null) {
            if (count < 0) {
                countDisplay.setTextColor(ContextCompat.getColor(this, R.color.nonchalant_red));
            } else {
                countDisplay.setTextColor(ContextCompat.getColor(this, R.color.black));
            }
        }
    }
}
