package com.adhiraj.lab01;

import android.view.ViewTreeObserver;
import java.util.Random;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;
import android.media.MediaPlayer;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ImageView;
import androidx.constraintlayout.widget.ConstraintLayout;

public class MainActivity extends AppCompatActivity {
    TextView greetingDisplay;
    ImageView rocket;
    ImageView rocketBR;
    ImageView star;

    ConstraintLayout mainLayout;
    private final Random random = new Random();
    private final float padding = 10f;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainLayout = findViewById(R.id.arcade_main_layout);
        greetingDisplay = findViewById(R.id.greeting_textview);

        rocket = findViewById(R.id.rocket_imageview);
        rocketBR = findViewById(R.id.rocket_imageview_br);
        star = findViewById(R.id.star_imageview); 

        if (star != null) {
            star.setVisibility(View.GONE); 
        }

        mainLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mainLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                createStarAnimation(10); 
                startFloatingAnimation(rocket);

                if (rocketBR != null) {
                    startFloatingAnimation(rocketBR);
                }
                createStarAnimationBottomRight(5);

                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    greetingDisplay.setText(getString(R.string.level_one_intro_text));
                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        Log.i("ArcadeTransition", "Transitioning to Main Menu (not implemented yet)");
                        // TODO: Implement Intent to MainMenuActivity here in a later step
                    }, 2000);
                }, 4000);
            }
        });
    }

    private void playSound() { 
        final MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.increment_button_click);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(MediaPlayer::release);
    }

    private void startFloatingAnimation(View view) {
        float maxDrift = 40f;
        float randomX = (random.nextFloat() - 0.5f) * 2 * maxDrift;
        float randomY = (random.nextFloat() - 0.5f) * 2 * maxDrift;
        float randomRotation = (random.nextFloat() - 0.5f) * 2 * 20;
        long randomDuration = random.nextInt(2000) + 3000;

        view.animate()
                .translationXBy(randomX)
                .translationYBy(randomY)
                .rotationBy(randomRotation)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setDuration(randomDuration)
                .withEndAction(() -> startFloatingAnimation(view));
    }

    private void startBoundedStarAnimation(View starView, int starSize) {
        if (mainLayout.getWidth() == 0 || mainLayout.getHeight() == 0) {
            starView.post(() -> startBoundedStarAnimation(starView, starSize));
            return;
        }

        int screenWidth = mainLayout.getWidth();
        int screenHeight = mainLayout.getHeight();

        float minX = padding;
        float maxX = screenWidth - starSize - padding;
        float minY = padding;
        float maxY = (screenHeight / 3f) - starSize - padding;

        if (maxX <= minX) maxX = minX + 1; 
        if (maxY <= minY) maxY = minY + 1; 

        float targetX = minX + random.nextFloat() * (maxX - minX);
        float targetY = minY + random.nextFloat() * (maxY - minY);
        float randomRotation = (random.nextFloat() - 0.5f) * 2 * 20;
        long randomDuration = random.nextInt(2000) + 3000;

        starView.animate()
                .x(targetX)
                .y(targetY)
                .rotationBy(randomRotation)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setDuration(randomDuration)
                .withEndAction(() -> startBoundedStarAnimation(starView, starSize));
    }

    private void createStarAnimation(int numberOfStars) {
        if (mainLayout.getWidth() == 0 || mainLayout.getHeight() == 0) {
            mainLayout.post(() -> createStarAnimation(numberOfStars));
            return;
        }
        int screenWidth = mainLayout.getWidth();
        int screenHeight = mainLayout.getHeight();

        for (int i = 0; i < numberOfStars; i++) {
            ImageView newStar = new ImageView(this);
            newStar.setImageResource(R.drawable.star);

            int size = random.nextInt(40) + 50;
            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(size, size);
            newStar.setLayoutParams(params);

            float initialMaxY = (screenHeight / 3f) - size - padding;
            if (initialMaxY < padding) initialMaxY = padding; 
            float initialY = padding + random.nextFloat() * (initialMaxY - padding > 0 ? initialMaxY - padding : 0);

            float initialMaxX = screenWidth - size - padding;
            if (initialMaxX < padding) initialMaxX = padding; 
            float initialX = padding + random.nextFloat() * (initialMaxX - padding > 0 ? initialMaxX - padding : 0);

            newStar.setX(initialX);
            newStar.setY(initialY);

            mainLayout.addView(newStar);
            startBoundedStarAnimation(newStar, size);
        }
    }

    private void startBoundedStarAnimationBottomRight(View starView, int starSize) {
        if (mainLayout.getWidth() == 0 || mainLayout.getHeight() == 0) {
            starView.post(() -> startBoundedStarAnimationBottomRight(starView, starSize));
            return;
        }

        int screenWidth = mainLayout.getWidth();
        int screenHeight = mainLayout.getHeight();

        float minX = screenWidth / 2f + padding;
        float maxX = screenWidth - starSize - padding;
        float minY = screenHeight * 2 / 3f + padding;
        float maxY = screenHeight - starSize - padding;

        if (maxX <= minX) maxX = minX + 1; 
        if (maxY <= minY) maxY = minY + 1; 

        float targetX = minX + random.nextFloat() * (maxX - minX);
        float targetY = minY + random.nextFloat() * (maxY - minY);
        float randomRotation = (random.nextFloat() - 0.5f) * 2 * 20;
        long randomDuration = random.nextInt(2000) + 3000;

        starView.animate()
                .x(targetX)
                .y(targetY)
                .rotationBy(randomRotation)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setDuration(randomDuration)
                .withEndAction(() -> startBoundedStarAnimationBottomRight(starView, starSize));
    }

    private void createStarAnimationBottomRight(int numberOfStars) {
        if (mainLayout.getWidth() == 0 || mainLayout.getHeight() == 0) {
            mainLayout.post(() -> createStarAnimationBottomRight(numberOfStars));
            return;
        }
        int screenWidth = mainLayout.getWidth();
        int screenHeight = mainLayout.getHeight();

        for (int i = 0; i < numberOfStars; i++) {
            ImageView newStar = new ImageView(this);
            newStar.setImageResource(R.drawable.star);

            int size = random.nextInt(30) + 40;
            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(size, size);
            newStar.setLayoutParams(params);

            float initialMinX = screenWidth / 2f + padding;
            float initialMaxX = screenWidth - size - padding;
            float initialX = initialMinX + random.nextFloat() * (initialMaxX - initialMinX > 0 ? initialMaxX - initialMinX : 0);

            float initialMinY = screenHeight * 2 / 3f + padding;
            float initialMaxY = screenHeight - size - padding;
            float initialY = initialMinY + random.nextFloat() * (initialMaxY - initialMinY > 0 ? initialMaxY - initialMinY : 0);
            
            if (initialX < 0) initialX = padding;
            if (initialY < 0) initialY = padding;
            if (initialX > screenWidth - size) initialX = screenWidth - size - padding;
            if (initialY > screenHeight - size) initialY = screenHeight - size - padding;

            newStar.setX(initialX);
            newStar.setY(initialY);

            mainLayout.addView(newStar);
            startBoundedStarAnimationBottomRight(newStar, size);
        }
    }
}
