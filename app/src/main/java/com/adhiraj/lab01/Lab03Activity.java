package com.adhiraj.lab01;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class Lab03Activity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener, SeekBar.OnSeekBarChangeListener {
    private TextView[] views;
    private SeekBar seekBar;
    private ConstraintLayout activityLayout;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static final String SHARED_PREFS_TAG = "com.tradan.lab03.sharedprefs";
    private static final String SEEKBAR_PROGRESS_KEY = "seekbar_progress";
    private int lastProgress;
    private List<Long> clickTimestamps;
    private static final long CPS_WINDOW_MS = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab03);

        
        sharedPreferences = getSharedPreferences(SHARED_PREFS_TAG, MODE_PRIVATE);
        editor = sharedPreferences.edit();

        
        activityLayout = findViewById(R.id.activity_lab03_layout);
        TextView topLeftTextView = findViewById(R.id.lab03_topleft_txtvw);
        TextView topRightTextView = findViewById(R.id.lab03_topright_txtvw);
        Button bottomLeftButton = findViewById(R.id.lab03_bottomleft_button);
        Button bottomRightButton = findViewById(R.id.lab03_bottomright_button);
        ImageButton backButton = findViewById(R.id.lab03_back_button);
        seekBar = findViewById(R.id.lab03_seekbar);

        
        views = new TextView[]{topLeftTextView, topRightTextView, bottomLeftButton, bottomRightButton};

        
        for (TextView view : views) {
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }
        backButton.setOnClickListener(this); 
        seekBar.setOnSeekBarChangeListener(this);

        
        clickTimestamps = new ArrayList<>();

        
    }

    private void setInitialValues() {
        
        for (TextView view : views) {
            String savedValue = sharedPreferences.getString(view.getTag().toString(), getString(R.string.lab03_initial_corner_view_text));
            view.setText(savedValue);
        }
        int savedProgress = sharedPreferences.getInt(SEEKBAR_PROGRESS_KEY, 30);
        seekBar.setProgress(savedProgress);
        
        for (TextView view : views) {
            view.setTextSize(savedProgress);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setInitialValues();
    }

    @Override
    protected void onPause() {
        super.onPause();
        
        editor.putInt(SEEKBAR_PROGRESS_KEY, seekBar.getProgress());
        editor.apply(); 
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.lab03_back_button) { 
            finish(); 
        } else if (v instanceof TextView) { 
            TextView clickedView = (TextView) v;
            try {
                int currentValue = Integer.parseInt(clickedView.getText().toString());
                currentValue++;
                clickedView.setText(String.valueOf(currentValue));

                
                editor.putString(clickedView.getTag().toString(), String.valueOf(currentValue));
                editor.apply();

                
                long currentTime = System.currentTimeMillis();
                clickTimestamps.add(currentTime);


                clickTimestamps.removeIf(aLong -> aLong < currentTime - CPS_WINDOW_MS);

                float currentCPS = 0;
                if (CPS_WINDOW_MS > 0) { 
                     currentCPS = (float) clickTimestamps.size() / (CPS_WINDOW_MS / 1000.0f);
                }
                Toast.makeText(this, String.format("CPS: %.2f", currentCPS), Toast.LENGTH_SHORT).show();

            } catch (NumberFormatException e) {
                
                clickedView.setText(getString(R.string.lab03_initial_corner_view_text)); 
                editor.putString(clickedView.getTag().toString(), getString(R.string.lab03_initial_corner_view_text));
                editor.apply();
            }
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (v.getId() == R.id.lab03_topleft_txtvw ||
            v.getId() == R.id.lab03_topright_txtvw || 
            v.getId() == R.id.lab03_bottomleft_button || 
            v.getId() == R.id.lab03_bottomright_button) {
            
            editor.clear().apply();
            
            setInitialValues();
            
            clickTimestamps.clear();
            Toast.makeText(this, "All data reset!", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            for (TextView view : views) {
                view.setTextSize(progress);
            }
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        lastProgress = seekBar.getProgress();
    }

    @Override
    public void onStopTrackingTouch(SeekBar sb) { 
        
        editor.putInt(SEEKBAR_PROGRESS_KEY, sb.getProgress());
        editor.apply();

        String message = String.format(getString(R.string.lab03_snackbar_font_changed), sb.getProgress());
        Snackbar snackbar = Snackbar.make(activityLayout, message, Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.lab03_snackbar_action_undo, view -> {
            sb.setProgress(lastProgress);

            for (TextView tv : views) {
                tv.setTextSize(lastProgress);
            }

            editor.putInt(SEEKBAR_PROGRESS_KEY, lastProgress);
            editor.apply();

            String revertedMessage = String.format(getString(R.string.lab03_snackbar_font_reverted), lastProgress);
            Snackbar.make(activityLayout, revertedMessage, Snackbar.LENGTH_SHORT).show();
        });
        snackbar.setActionTextColor(Color.CYAN); 
        snackbar.show();
    }
}
