package com.adhiraj.lab01;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivityLifecycle";
    private static final String PREFS_NAME = "ArcadeSettingsPrefs";
    private static final String PREF_HIGH_SCORE = "highScore";
    private static final String PREF_POWER_UP_INDEX = "powerUpIndex";
    private static final String PREF_ROCKET_VISIBLE = "rocketVisible";
    private static final String PREF_STARS_VISIBLE = "starsVisible";

    private ConstraintLayout mainLayout;
    private ImageView rocketImageView;
    private TextView greetingDisplay;
    private Button playButton;
    private ImageButton settingsButton;

    private int screenWidth;
    private int screenHeight;

    private boolean isRocketVisibleSetting = true;
    private boolean isStarsVisible = true;
    private int currentPowerUpIndex = 0;
    private int highScore = 0;
    private String[] powerUps;

    private Star starManager;
    private Rocket rocketManager;

    private Handler interactionHandler;
    private Runnable interactionCheckRunnable;
    private static final long INTERACTION_CHECK_INTERVAL = 100;
    private static final float TRANSPARENT_ALPHA = 0.3f;
    private static final float OPAQUE_ALPHA = 1.0f;

    private final int rocketAnimationPadding = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate called");
        setContentView(R.layout.activity_main);

        interactionHandler = new Handler();

        mainLayout = findViewById(R.id.arcade_main_layout);
        greetingDisplay = findViewById(R.id.greeting_textview);
        rocketImageView = findViewById(R.id.rocket_imageview);
        playButton = findViewById(R.id.play_button);
        settingsButton = findViewById(R.id.settings_button);

        powerUps = getResources().getStringArray(R.array.power_ups_array);
        
        rocketManager = new Rocket(rocketImageView, mainLayout);

        if (settingsButton != null) {
            settingsButton.setOnClickListener(v -> {
                Log.d(TAG, "Settings button clicked");
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            });
        } else {
            Log.e(TAG, "Settings button is null!");
        }

        if (playButton != null) {
            playButton.setOnClickListener(v -> {
                Log.i(TAG, "Play button clicked! Starting MainMenuActivity.");
                Intent intent = new Intent(MainActivity.this, MainMenuActivity.class);
                startActivity(intent);
            });
        }

        ViewTreeObserver viewTreeObserver = mainLayout.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mainLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    screenWidth = mainLayout.getWidth();
                    screenHeight = mainLayout.getHeight();
                    Log.d(TAG, "GlobalLayout: screenWidth=" + screenWidth + ", screenHeight=" + screenHeight);

                    loadAndApplySettings();

                    if (starManager == null && screenWidth > 0 && screenHeight > 0) {
                        starManager = new Star(MainActivity.this, mainLayout, screenWidth, screenHeight);
                    }
                    if (starManager != null) {
                        starManager.setStarsVisible(isStarsVisible);

                        initializeAndStartInteractionChecks();
                    }
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume called");

        if (mainLayout.getWidth() > 0 && mainLayout.getHeight() > 0) {
            screenWidth = mainLayout.getWidth();
            screenHeight = mainLayout.getHeight();
        } else {
             Log.w(TAG, "onResume: mainLayout dimensions not available yet.");
        }

        loadAndApplySettings();

        if (rocketManager != null && screenWidth > 0 && screenHeight > 0) {
             Log.d(TAG, "onResume: Updating rocket animating state with current screen dimensions: " + screenWidth + "x" + screenHeight);
            rocketManager.setAnimatingState(isRocketVisibleSetting, screenWidth, screenHeight, rocketAnimationPadding);
        }

        if (starManager != null) {
            starManager.updateScreenDimensions(screenWidth, screenHeight);
            starManager.setStarsVisible(isStarsVisible);
        } else if (screenWidth > 0 && screenHeight > 0) {
            starManager = new Star(MainActivity.this, mainLayout, screenWidth, screenHeight);
            starManager.setStarsVisible(isStarsVisible);
            Log.d(TAG, "onResume: starManager initialized and visibility set.");
        }
        
        if (interactionCheckRunnable != null && interactionHandler != null && isStarsVisible) {
            interactionHandler.removeCallbacks(interactionCheckRunnable);
            interactionHandler.post(interactionCheckRunnable);
            Log.d(TAG, "Interaction checks (re)started in onResume.");
        } else if (!isStarsVisible && interactionHandler != null && interactionCheckRunnable != null) {
            interactionHandler.removeCallbacks(interactionCheckRunnable);
            Log.d(TAG, "Interaction checks stopped in onResume as stars are not visible.");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause called");
        if (interactionHandler != null && interactionCheckRunnable != null) {
            interactionHandler.removeCallbacks(interactionCheckRunnable);
            Log.d(TAG, "Interaction checks stopped in onPause.");
        }
        if (rocketManager != null) {
            Log.d(TAG, "onPause: Telling rocketManager to stop its animation.");
            rocketManager.stopAnimation(); 
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy called");
        if (rocketManager != null) {
             Log.d(TAG, "onDestroy: Telling rocketManager to stop its animation.");
            rocketManager.stopAnimation();
        }
        if (starManager != null) {
            starManager.setStarsVisible(false);
        }
        if (interactionHandler != null && interactionCheckRunnable != null) {
            interactionHandler.removeCallbacks(interactionCheckRunnable);
        }
    }

    private void loadAndApplySettings() {
        Log.d(TAG, "loadAndApplySettings called");
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        isRocketVisibleSetting = prefs.getBoolean(PREF_ROCKET_VISIBLE, true);
        isStarsVisible = prefs.getBoolean(PREF_STARS_VISIBLE, true);
        currentPowerUpIndex = prefs.getInt(PREF_POWER_UP_INDEX, 0);
        highScore = prefs.getInt(PREF_HIGH_SCORE, 0);

        Log.d(TAG, "Loaded settings: isRocketVisibleSetting=" + isRocketVisibleSetting + ", isStarsVisible=" + isStarsVisible);

        if (rocketManager != null) {
            Log.d(TAG, "loadAndApplySettings: Setting rocket animating state. Screen: " + screenWidth + "x" + screenHeight);
            rocketManager.setAnimatingState(isRocketVisibleSetting, screenWidth, screenHeight, rocketAnimationPadding);
        }

        if (starManager != null) {
            starManager.setStarsVisible(isStarsVisible);
        }
    }

    private boolean checkCollision(View view1, View view2) {
        if (view1 == null || view2 == null || view1.getVisibility() != View.VISIBLE || view2.getVisibility() != View.VISIBLE) {
            return false;
        }
        Rect rect1 = new Rect();
        view1.getHitRect(rect1);
        Rect rect2 = new Rect();
        view2.getHitRect(rect2);
        return Rect.intersects(rect1, rect2);
    }

    private boolean isStarNearScreenCenter(View starView) {
        if (starView == null || screenWidth == 0 || screenHeight == 0 || starView.getWidth() == 0 || starView.getHeight() == 0) {
            return false;
        }
        float starCenterX = starView.getX() + (starView.getWidth() / 2f);
        float starCenterY = starView.getY() + (starView.getHeight() / 2f);
        float centerXMin = screenWidth / 3f;
        float centerXMax = 2 * screenWidth / 3f;
        float centerYMin = screenHeight / 3f;
        float centerYMax = 2 * screenHeight / 3f;

        return starCenterX >= centerXMin && starCenterX <= centerXMax &&
                starCenterY >= centerYMin && starCenterY <= centerYMax;
    }

    private void initializeAndStartInteractionChecks() {
        if (interactionCheckRunnable != null) {
            if (isStarsVisible && interactionHandler != null) {
                interactionHandler.removeCallbacks(interactionCheckRunnable);
                interactionHandler.post(interactionCheckRunnable);
                Log.d(TAG, "Interaction checks re-initialized and started.");
            } else if (!isStarsVisible && interactionHandler != null) {
                interactionHandler.removeCallbacks(interactionCheckRunnable);
                Log.d(TAG, "Interaction checks stopped as stars are not visible during re-init.");
            }
            return;
        }

        interactionCheckRunnable = new Runnable() {
            @Override
            public void run() {
                if (starManager == null || !isStarsVisible || starManager.getStarViews().isEmpty() ||
                        greetingDisplay == null || playButton == null || mainLayout.getWidth() == 0) {
                    if (interactionHandler != null) {
                        interactionHandler.postDelayed(this, INTERACTION_CHECK_INTERVAL);
                    }
                    return;
                }

                for (View starView : starManager.getStarViews()) {
                    if (starView.getVisibility() != View.VISIBLE) {
                        continue;
                    }

                    boolean intersectsWithUI = checkCollision(starView, greetingDisplay) || checkCollision(starView, playButton);
                    boolean nearCenter = isStarNearScreenCenter(starView);

                    if (intersectsWithUI && nearCenter) {
                        starView.setAlpha(TRANSPARENT_ALPHA);
                    } else {
                        starView.setAlpha(OPAQUE_ALPHA);
                    }
                }

                if (interactionHandler != null) {
                    interactionHandler.postDelayed(this, INTERACTION_CHECK_INTERVAL);
                }
            }
        };

        if (isStarsVisible && interactionHandler != null) {
            interactionHandler.post(interactionCheckRunnable);
            Log.d(TAG, "Interaction checks initialized and started for the first time.");
        } else {
            Log.d(TAG, "Interaction checks not started initially as stars are not visible.");
        }
    }
}
