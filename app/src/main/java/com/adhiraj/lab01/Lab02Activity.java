package com.adhiraj.lab01;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton; 
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class Lab02Activity extends AppCompatActivity {

    TextView greetingTextView;
    TextView likesDisplayTextView;
    TextView planetDisplayTextView;
    Button incrementButton;
    Button decrementButton;
    ImageButton lab02BackButton; 
    RadioGroup directionRadioGroup;
    RadioButton radioUp;
    RadioButton radioDown;

    String[] planetsArray;
    int count = 0; 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab02);

        greetingTextView = findViewById(R.id.greeting_textview);
        likesDisplayTextView = findViewById(R.id.likes_display_textview);
        planetDisplayTextView = findViewById(R.id.planet_display_textview);
        incrementButton = findViewById(R.id.increment_button);
        decrementButton = findViewById(R.id.decrement_button);
        lab02BackButton = findViewById(R.id.lab02_back_button); 
        directionRadioGroup = findViewById(R.id.direction_radiogroup);
        radioUp = findViewById(R.id.radio_up);
        radioDown = findViewById(R.id.radio_down);

        greetingTextView.setText(Html.fromHtml(getString(R.string.lab02_greeting_html_formatted), Html.FROM_HTML_MODE_LEGACY));

        planetsArray = getResources().getStringArray(R.array.planets_array);

        updateLikesDisplay();
        updatePlanetDisplay();

        View.OnClickListener planetCycleListener = v -> {
            int viewId = v.getId();
            if (viewId == R.id.increment_button) {
                if (radioUp.isChecked()) {
                    count++;
                    count %= planetsArray.length;
                }
            } else if (viewId == R.id.decrement_button) {
                if (radioDown.isChecked()) {
                    count--;
                    if (count < 0) {
                        count = planetsArray.length - 1;
                    }
                }
            }
            updateLikesDisplay();
            updatePlanetDisplay();
        };

        incrementButton.setOnClickListener(planetCycleListener);
        decrementButton.setOnClickListener(planetCycleListener);

        lab02BackButton.setOnClickListener(v -> finish());
    }

    private void updateLikesDisplay() {
        likesDisplayTextView.setText(getString(R.string.likes_count, count));
    }

    private void updatePlanetDisplay() {
        if (planetsArray != null && planetsArray.length > 0) {
            
            int safeIndex = count;
            if (safeIndex < 0) safeIndex = planetsArray.length -1; 
            safeIndex = safeIndex % planetsArray.length; 
            planetDisplayTextView.setText(planetsArray[safeIndex]);
        }
    }
}
