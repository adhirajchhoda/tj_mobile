package com.adhiraj.lab01;

import static kotlin.reflect.KTypeProjection.star;
import android.view.ViewTreeObserver;
import java.util.Random;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.media.MediaPlayer;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.widget.ImageView;
import androidx.constraintlayout.widget.ConstraintLayout;

public class MainActivity extends AppCompatActivity {
    Button incrementButton;
    TextView greetingDisplay;
    TextView countDisplay;
    ImageView rocket;
    ImageView star;

    ConstraintLayout mainLayout;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainLayout = findViewById(R.id.main);
        incrementButton = findViewById(R.id.increment_button);
        countDisplay = findViewById(R.id.count_textview);
        greetingDisplay = findViewById(R.id.greeting_textview);

        rocket = findViewById(R.id.rocket_imageview);
        star = findViewById(R.id.star_imageview);
        incrementButton.setOnClickListener(v -> {
            playSound();
            Log.i("testing: incrementing", String.valueOf(++count));
            countDisplay.setText(String.valueOf(count));
            updateCountDisplayColor();
        });

        mainLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mainLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                createStarAnimation(10);
                startFloatingAnimation(rocket);
            };
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
            countDisplay.setTextColor(ContextCompat.getColor(this, R.color.white));
        }
    }
    private void playSound() {
        final MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.increment_button_click);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(mp -> {
            mp.release();
        });
    }
    private void startFloatingAnimation(View view) {
        float maxDrift = 40f;
        Random random = new Random();
        float randomX = (random.nextFloat() - 0.5f) * 2 * maxDrift;
        float randomY = (random.nextFloat() - 0.5f) * 2 * maxDrift;
        float randomRotation = (random.nextFloat() - 0.5f) * 2 * 20;
        long randomDuration = random.nextInt(2000) + 3000;

        view.animate()
                .translationX(randomX)
                .translationY(randomY)
                .rotation(randomRotation)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setDuration(randomDuration)
                .withEndAction(() -> {
                    startFloatingAnimation(view);
                });
    }

    private void createStarAnimation(int numberOfStars) {
        int screenWidth = mainLayout.getWidth();
        int screenHeight = mainLayout.getHeight();
        Random random = new Random();

        for (int i = 0; i < numberOfStars; i++) {
            ImageView newStar = new ImageView(this);
            newStar.setImageResource(R.drawable.star);

            int size = random.nextInt(40) + 50;
            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(size, size);
            newStar.setLayoutParams(params);

            if (random.nextBoolean()) {
                float x = random.nextFloat() * (screenWidth - size);
                float y = random.nextFloat() * (screenHeight / 4f);
                newStar.setX(x);
                newStar.setY(y);

            } else {
                float x = (screenWidth / 2f) + (random.nextFloat() * (screenWidth / 2f - size));
                float y = (screenHeight * 0.6f) + (random.nextFloat() * (screenHeight * 0.4f - size));
                newStar.setX(x);
                newStar.setY(y);
            }
            mainLayout.addView(newStar);
            startFloatingAnimation(newStar);
        }
    }
}