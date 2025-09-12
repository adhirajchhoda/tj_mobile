package com.adhiraj.lab01;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Path;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.LinearInterpolator; // Changed for floating effect
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ConstraintLayout mainLayout;
    private ImageView rocketImageView;
    // private ImageView rocketImageViewBR; // Removed second rocket
    private TextView greetingDisplay;
    private Button playButton;

    private final List<View> starViews = new ArrayList<>();
    private final Random random = new Random();
    private final int padding = 100;

    private static final int TOP_REGION_STARS = 10;
    private static final int BOTTOM_RIGHT_REGION_STARS = 5;
    private static final int DEFAULT_STAR_SIZE_DP = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainLayout = findViewById(R.id.arcade_main_layout);
        greetingDisplay = findViewById(R.id.greeting_textview);
        rocketImageView = findViewById(R.id.rocket_imageview);playButton = findViewById(R.id.play_button);

        playButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MainMenuActivity.class);
            startActivity(intent);
            finish();
        });

        ViewTreeObserver viewTreeObserver = mainLayout.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mainLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                    if (mainLayout.getWidth() == 0 || mainLayout.getHeight() == 0) {
                        mainLayout.post(this::onGlobalLayout);
                        return;
                    }

                    int screenWidth = mainLayout.getWidth();
                    int screenHeight = mainLayout.getHeight();

                    animateRocketPerimeter(rocketImageView, screenWidth, screenHeight, 0);

                    createStarAnimationTopRegion(TOP_REGION_STARS);
                    createStarAnimationBottomRightRegion(BOTTOM_RIGHT_REGION_STARS);
                }
            });
        }
    }

    private void animateRocketPerimeter(ImageView rocket, int screenWidth, int screenHeight, long startDelay) {
        if (rocket.getWidth() == 0 || rocket.getHeight() == 0) {
            rocket.post(() -> animateRocketPerimeter(rocket, screenWidth, screenHeight, startDelay));
            return;
        }

        int rocketWidth = rocket.getWidth();
        int rocketHeight = rocket.getHeight();

        PointF p0 = new PointF(padding, padding);
        PointF p1 = new PointF(screenWidth - rocketWidth - padding, padding);
        PointF p2 = new PointF(screenWidth - rocketWidth - padding, screenHeight - rocketHeight - padding);
        PointF p3 = new PointF(padding, screenHeight - rocketHeight - padding);

        rocket.setX(p0.x);
        rocket.setY(p0.y);
        rocket.setRotation(0); // Initial rotation state

        long edgeDuration = 8000; // Increased for slower movement
        float curveFactor = 250f; // Increased for curvier paths

        Path path0 = new Path();
        path0.moveTo(p0.x, p0.y);
        path0.quadTo(p0.x + (p1.x - p0.x) / 2, p0.y - curveFactor, p1.x, p1.y);

        Path path1 = new Path();
        path1.moveTo(p1.x, p1.y);
        path1.quadTo(p1.x + curveFactor, p1.y + (p2.y - p1.y) / 2, p2.x, p2.y);

        Path path2 = new Path();
        path2.moveTo(p2.x, p2.y);
        path2.quadTo(p2.x - (p2.x - p3.x) / 2, p2.y + curveFactor, p3.x, p3.y);

        Path path3 = new Path();
        path3.moveTo(p3.x, p3.y);
        path3.quadTo(p3.x - curveFactor, p3.y - (p3.y - p0.y) / 2, p0.x, p0.y);

        ObjectAnimator move0 = ObjectAnimator.ofFloat(rocket, View.X, View.Y, path0);
        move0.setDuration(edgeDuration);
        move0.setInterpolator(new LinearInterpolator());

        ObjectAnimator move1 = ObjectAnimator.ofFloat(rocket, View.X, View.Y, path1);
        move1.setDuration(edgeDuration);
        move1.setInterpolator(new LinearInterpolator());

        ObjectAnimator move2 = ObjectAnimator.ofFloat(rocket, View.X, View.Y, path2);
        move2.setDuration(edgeDuration);
        move2.setInterpolator(new LinearInterpolator());

        ObjectAnimator move3 = ObjectAnimator.ofFloat(rocket, View.X, View.Y, path3);
        move3.setDuration(edgeDuration);
        move3.setInterpolator(new LinearInterpolator());

        // Rotation animators: duration changed to edgeDuration
        ObjectAnimator rotateTo90 = ObjectAnimator.ofFloat(rocket, "rotation", 0, 90);
        rotateTo90.setDuration(edgeDuration); // Use edgeDuration for gradual turn
        rotateTo90.setInterpolator(new LinearInterpolator());

        ObjectAnimator rotateTo180 = ObjectAnimator.ofFloat(rocket, "rotation", 90, 180);
        rotateTo180.setDuration(edgeDuration); // Use edgeDuration for gradual turn
        rotateTo180.setInterpolator(new LinearInterpolator());

        ObjectAnimator rotateTo270 = ObjectAnimator.ofFloat(rocket, "rotation", 180, 270);
        rotateTo270.setDuration(edgeDuration); // Use edgeDuration for gradual turn
        rotateTo270.setInterpolator(new LinearInterpolator());

        ObjectAnimator rotateTo360 = ObjectAnimator.ofFloat(rocket, "rotation", 270, 360);
        rotateTo360.setDuration(edgeDuration); // Use edgeDuration for gradual turn
        rotateTo360.setInterpolator(new LinearInterpolator());

        // AnimatorSet to play movement and rotation together for each segment
        AnimatorSet segment0Animator = new AnimatorSet();
        segment0Animator.playTogether(move0, rotateTo90);

        AnimatorSet segment1Animator = new AnimatorSet();
        segment1Animator.playTogether(move1, rotateTo180);

        AnimatorSet segment2Animator = new AnimatorSet();
        segment2Animator.playTogether(move2, rotateTo270);

        AnimatorSet segment3Animator = new AnimatorSet();
        segment3Animator.playTogether(move3, rotateTo360);
        
        final AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(
                segment0Animator,
                segment1Animator,
                segment2Animator,
                segment3Animator
        );

        animatorSet.setStartDelay(startDelay);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                rocket.setRotation(0); // Reset rotation for the next loop
                animatorSet.start(); // Loop the animation
            }
        });
        animatorSet.start();
    }

    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }

    private void createStarAnimationTopRegion(int numberOfStars) {
        if (mainLayout.getWidth() == 0 || mainLayout.getHeight() == 0) {
            mainLayout.post(() -> createStarAnimationTopRegion(numberOfStars));
            return;
        }

        int screenWidth = mainLayout.getWidth();
        int screenHeight = mainLayout.getHeight();
        int starSizePx = dpToPx(DEFAULT_STAR_SIZE_DP);

        for (int i = 0; i < numberOfStars; i++) {
            ImageView star = new ImageView(this);
            star.setImageResource(R.drawable.star);
            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(starSizePx, starSizePx);
            star.setLayoutParams(params);

            float initialMaxY = (screenHeight / 3f) - starSizePx - padding;
            float initialMaxX = screenWidth - starSizePx - padding;

            if (initialMaxX <= padding) initialMaxX = padding + 1;
            if (initialMaxY <= padding) initialMaxY = padding + 1;

            float initialX = padding + random.nextFloat() * (initialMaxX - padding);
            float initialY = padding + random.nextFloat() * (initialMaxY - padding);

            star.setX(Math.max(padding, Math.min(initialX, screenWidth - starSizePx - padding)));
            star.setY(Math.max(padding, Math.min(initialY, (screenHeight / 3f) - starSizePx - padding)));

            mainLayout.addView(star);
            starViews.add(star);
            startBoundedStarAnimationTopRegion(star, starSizePx);
        }
    }

    private void startBoundedStarAnimationTopRegion(View starView, int starSize) {
        if (mainLayout.getWidth() == 0 || mainLayout.getHeight() == 0) {
            mainLayout.post(() -> startBoundedStarAnimationTopRegion(starView, starSize));
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
        float rotation = random.nextFloat() * 360;
        long duration = 3000 + random.nextInt(4000);

        starView.animate()
                .x(targetX)
                .y(targetY)
                .rotation(rotation)
                .setDuration(duration)
                .withEndAction(() -> {
                    if (starView.getParent() != null) {
                        startBoundedStarAnimationTopRegion(starView, starSize);
                    }
                })
                .start();
    }

    private void createStarAnimationBottomRightRegion(int numberOfStars) {
        if (mainLayout.getWidth() == 0 || mainLayout.getHeight() == 0) {
            mainLayout.post(() -> createStarAnimationBottomRightRegion(numberOfStars));
            return;
        }

        int screenWidth = mainLayout.getWidth();
        int screenHeight = mainLayout.getHeight();
        int starSizePx = dpToPx(DEFAULT_STAR_SIZE_DP);

        for (int i = 0; i < numberOfStars; i++) {
            ImageView star = new ImageView(this);
            star.setImageResource(R.drawable.star);
            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(starSizePx, starSizePx);
            star.setLayoutParams(params);

            float initialMinX = screenWidth / 2f + padding;
            float initialMaxX = screenWidth - starSizePx - padding;
            float initialMinY = screenHeight * 2 / 3f + padding;
            float initialMaxY = screenHeight - starSizePx - padding;

            if (initialMaxX <= initialMinX) initialMaxX = initialMinX + 1;
            if (initialMaxY <= initialMinY) initialMaxY = initialMinY + 1;

            float initialX = initialMinX + random.nextFloat() * (initialMaxX - initialMinX);
            float initialY = initialMinY + random.nextFloat() * (initialMaxY - initialMinY);

            initialX = Math.max(initialMinX, Math.min(initialX, initialMaxX));
            initialY = Math.max(initialMinY, Math.min(initialY, initialMaxY));

            if (initialX < 0) initialX = padding;
            if (initialY < 0) initialY = padding;
            if (initialX > screenWidth - starSizePx) initialX = screenWidth - starSizePx - padding;
            if (initialY > screenHeight - starSizePx) initialY = screenHeight - starSizePx - padding;

            star.setX(initialX);
            star.setY(initialY);

            mainLayout.addView(star);
            starViews.add(star);
            startBoundedStarAnimationBottomRightRegion(star, starSizePx);
        }
    }

    private void startBoundedStarAnimationBottomRightRegion(View starView, int starSize) {
        if (mainLayout.getWidth() == 0 || mainLayout.getHeight() == 0) {
            mainLayout.post(() -> startBoundedStarAnimationBottomRightRegion(starView, starSize));
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
        float rotation = random.nextFloat() * 360;
        long duration = 3000 + random.nextInt(4000);

        starView.animate()
                .x(targetX)
                .y(targetY)
                .rotation(rotation)
                .setDuration(duration)
                .withEndAction(() -> {
                    if (starView.getParent() != null) {
                        startBoundedStarAnimationBottomRightRegion(starView, starSize);
                    }
                })
                .start();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (View starView : starViews) {
            starView.animate().cancel();
            if (starView.getParent() != null) {
                ((ConstraintLayout) starView.getParent()).removeView(starView);
            }
        }
        starViews.clear();
        if (rocketImageView != null) rocketImageView.animate().cancel();
\    }
}
