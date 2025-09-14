package com.adhiraj.lab01;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = "SettingsActivityLifecycle";
    private static final String PREFS_NAME = "ArcadeSettingsPrefs";
    private static final String PREF_HIGH_SCORE = "highScore";
    private static final String PREF_POWER_UP_INDEX = "powerUpIndex";
    private static final String PREF_ROCKET_VISIBLE = "rocketVisible";
    private static final String PREF_STARS_VISIBLE = "starsVisible";

    private TextView highScoreTextView;
    private TextView powerUpTextView;
    private Button prevPowerUpButton;
    private Button nextPowerUpButton;
    private SwitchCompat rocketVisibleSwitch;
    private SwitchCompat starsVisibleSwitch;
    private Button applyButton;
    private Button resetButton; 

    private String[] powerUps;
    private int currentPowerUpIndex = 0;
    private int highScore = 0;
    private boolean isRocketVisible = true;
    private boolean isStarsVisible = true;

    
    private static final int DEFAULT_POWER_UP_INDEX = 0;
    private static final boolean DEFAULT_ROCKET_VISIBLE = true;
    private static final boolean DEFAULT_STARS_VISIBLE = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate called");
        setContentView(R.layout.activity_settings);

        highScoreTextView = findViewById(R.id.settings_high_score_textview);
        powerUpTextView = findViewById(R.id.settings_power_up_textview);
        prevPowerUpButton = findViewById(R.id.settings_prev_power_up_button);
        nextPowerUpButton = findViewById(R.id.settings_next_power_up_button);
        rocketVisibleSwitch = findViewById(R.id.settings_rocket_visible_switch);
        starsVisibleSwitch = findViewById(R.id.settings_stars_visible_switch);
        applyButton = findViewById(R.id.settings_apply_button);
        resetButton = findViewById(R.id.settings_reset_button); 

        powerUps = getResources().getStringArray(R.array.power_ups_array);

        loadSettings();
        updateHighScoreDisplay();
        updatePowerUpDisplay();
        updateSwitchStates(); 

        prevPowerUpButton.setOnClickListener(v -> {
            currentPowerUpIndex--;
            if (currentPowerUpIndex < 0) {
                currentPowerUpIndex = powerUps.length - 1;
            }
            updatePowerUpDisplay();
            saveSettings(); 
        });

        nextPowerUpButton.setOnClickListener(v -> {
            currentPowerUpIndex++;
            if (currentPowerUpIndex >= powerUps.length) {
                currentPowerUpIndex = 0;
            }
            updatePowerUpDisplay();
            saveSettings(); 
        });

        rocketVisibleSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isRocketVisible = isChecked;
            saveSettings(); 
        });

        starsVisibleSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isStarsVisible = isChecked;
            saveSettings(); 
        });

        applyButton.setOnClickListener(v -> {
            Log.d(TAG, "Apply button clicked");
            finish();
        });

        
        resetButton.setOnClickListener(v -> {
            resetSettingsToDefault();
            saveSettings();
            loadSettings(); 
            updatePowerUpDisplay();
            updateSwitchStates();
            Toast.makeText(SettingsActivity.this, "Settings Reset!", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Reset button clicked and settings reset");
        });
    }

    private void loadSettings() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        highScore = prefs.getInt(PREF_HIGH_SCORE, 0); 
        currentPowerUpIndex = prefs.getInt(PREF_POWER_UP_INDEX, DEFAULT_POWER_UP_INDEX);
        isRocketVisible = prefs.getBoolean(PREF_ROCKET_VISIBLE, DEFAULT_ROCKET_VISIBLE);
        isStarsVisible = prefs.getBoolean(PREF_STARS_VISIBLE, DEFAULT_STARS_VISIBLE);

        Log.d(TAG, "Loaded settings: highScore=" + highScore + ", powerUpIndex=" + currentPowerUpIndex + ", rocketVisible=" + isRocketVisible + ", starsVisible=" + isStarsVisible);
    }

    private void saveSettings() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(PREF_POWER_UP_INDEX, currentPowerUpIndex);
        editor.putBoolean(PREF_ROCKET_VISIBLE, isRocketVisible);
        editor.putBoolean(PREF_STARS_VISIBLE, isStarsVisible);
        
        editor.apply();
        Log.d(TAG, "Settings saved!");
        
    }

    private void resetSettingsToDefault() {
        currentPowerUpIndex = DEFAULT_POWER_UP_INDEX;
        isRocketVisible = DEFAULT_ROCKET_VISIBLE;
        isStarsVisible = DEFAULT_STARS_VISIBLE;
        
        
        Log.d(TAG, "Settings reset to defaults.");
    }

    private void updateHighScoreDisplay() {
        if (highScoreTextView != null) {
            highScoreTextView.setText(getString(R.string.high_score_text, highScore));
        }
    }

    private void updatePowerUpDisplay() {
        if (powerUpTextView != null && powerUps != null && powerUps.length > 0) {
            if (currentPowerUpIndex < 0 || currentPowerUpIndex >= powerUps.length) {
                currentPowerUpIndex = DEFAULT_POWER_UP_INDEX; 
            }
            powerUpTextView.setText(powerUps[currentPowerUpIndex]);
        }
    }

    private void updateSwitchStates() {
        rocketVisibleSwitch.setChecked(isRocketVisible);
        starsVisibleSwitch.setChecked(isStarsVisible);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause called, saving settings.");
        saveSettings();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume called");
        loadSettings();
        updateHighScoreDisplay();
        updatePowerUpDisplay();
        updateSwitchStates();
    }
}
