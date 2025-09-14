package com.adhiraj.lab01;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import java.util.Random;

public class Rocket {

    private static final String TAG = "Rocket";

    private ImageView rocketImageView;
    private ConstraintLayout mainLayout; 
    private AnimatorSet currentAnimatorSet;
    private int animationPadding;

    private boolean shouldBeAnimating = false;
    private int currentScreenWidth;
    private int currentScreenHeight;
    private Random random = new Random();


    public Rocket(ImageView rocketImageView, ConstraintLayout mainLayout) {
        this.rocketImageView = rocketImageView;
        this.mainLayout = mainLayout;
    }

    public void setAnimatingState(boolean shouldAnimate, int screenWidth, int screenHeight, int padding) {
        Log.d(TAG, "setAnimatingState called: shouldAnimate=" + shouldAnimate + ", screenWidth=" + screenWidth + ", screenHeight=" + screenHeight + ", padding=" + padding);
        this.shouldBeAnimating = shouldAnimate;
        this.currentScreenWidth = screenWidth;
        this.currentScreenHeight = screenHeight;
        this.animationPadding = padding;

        updateAnimationStatusInternal();
    }

    private void updateAnimationStatusInternal() {
        if (rocketImageView == null) {
            Log.e(TAG, "updateAnimationStatusInternal: Rocket ImageView is null!");
            return;
        }

        if (shouldBeAnimating) {
            rocketImageView.setVisibility(View.VISIBLE);
            if (currentScreenWidth == 0 || currentScreenHeight == 0 || rocketImageView.getWidth() == 0 || rocketImageView.getHeight() == 0) {
                Log.d(TAG, "Rocket or screen not ready for animation, postponing. Rocket W/H: " + rocketImageView.getWidth() + "/" + rocketImageView.getHeight() + " Screen: " + currentScreenWidth + "x" + currentScreenHeight);
                rocketImageView.post(() -> {
                    if (mainLayout != null) {
                        this.currentScreenWidth = mainLayout.getWidth();
                        this.currentScreenHeight = mainLayout.getHeight();
                    }
                    if (this.currentScreenWidth > 0 && this.currentScreenHeight > 0 && rocketImageView.getWidth() > 0 && rocketImageView.getHeight() > 0) {
                        Log.d(TAG, "Retrying animation setup. New dimensions: " + this.currentScreenWidth + "x" + this.currentScreenHeight);
                        updateAnimationStatusInternal(); 
                    } else {
                        Log.w(TAG, "Still no valid screen or rocket dimensions on retry. Screen: " + this.currentScreenWidth + "x" + this.currentScreenHeight + " Rocket W/H: " + rocketImageView.getWidth() + "/" + rocketImageView.getHeight());
                    }
                });
                return; 
            }
            Log.d(TAG, "Dimensions ready, proceeding to start/restart animation.");
            animatePerimeterInternal(0);

        } else { 
            Log.d(TAG, "setAnimatingState: shouldBeAnimating is false. Cancelling animation and setting GONE.");
            cancelAnimationInternal(); 
            rocketImageView.setVisibility(View.GONE);
        }
    }

    private float calculateDynamicCurveFactor() {
        if (animationPadding <= 0) return 0f; 
        
        float factor = (random.nextFloat() * 0.8f) + 0.1f; 
        float curve = animationPadding * factor;

        
        if (animationPadding >= 20) { 
            return Math.max(10f, curve);
        } else { 
            return curve;
        }
    }


    private void animatePerimeterInternal(long startDelay) {
        if (rocketImageView == null) {
            Log.e(TAG, "animatePerimeterInternal: Rocket ImageView is null!");
            return;
        }

        if (this.currentAnimatorSet != null && this.currentAnimatorSet.isRunning()) {
            Log.d(TAG, "animatePerimeterInternal: Existing animation running, cancelling it first.");
            this.currentAnimatorSet.removeAllListeners(); 
            this.currentAnimatorSet.cancel();
            
        }
        
        int rocketWidth = rocketImageView.getWidth();
        int rocketHeight = rocketImageView.getHeight();

        if (rocketWidth == 0 || rocketHeight == 0) {
            Log.e(TAG, "animatePerimeterInternal: Rocket dimensions are zero, cannot animate.");
            shouldBeAnimating = false; 
            rocketImageView.setVisibility(View.GONE);
            return;
        }

        PointF p0 = new PointF(animationPadding, animationPadding);
        PointF p1 = new PointF(currentScreenWidth - rocketWidth - animationPadding, animationPadding);
        PointF p2 = new PointF(currentScreenWidth - rocketWidth - animationPadding, currentScreenHeight - rocketHeight - animationPadding);
        PointF p3 = new PointF(animationPadding, currentScreenHeight - rocketHeight - animationPadding);

        rocketImageView.setX(p0.x);
        rocketImageView.setY(p0.y);
        rocketImageView.setRotation(90); 

        long edgeDuration = 7000L; 
        long rotationDuration = 500;
        
        float c0 = calculateDynamicCurveFactor();
        float c1 = calculateDynamicCurveFactor();
        float c2 = calculateDynamicCurveFactor();
        float c3 = calculateDynamicCurveFactor();

        Path path0 = new Path();
        path0.moveTo(p0.x, p0.y);
        path0.quadTo(p0.x + (p1.x - p0.x) / 2, p0.y - c0, p1.x, p1.y);

        Path path1 = new Path();
        path1.moveTo(p1.x, p1.y);
        path1.quadTo(p1.x + c1, p1.y + (p2.y - p1.y) / 2, p2.x, p2.y);

        Path path2 = new Path();
        path2.moveTo(p2.x, p2.y);
        path2.quadTo(p2.x - (p2.x - p3.x) / 2, p2.y + c2, p3.x, p3.y);

        Path path3 = new Path();
        path3.moveTo(p3.x, p3.y);
        path3.quadTo(p3.x - c3, p3.y - (p3.y - p0.y) / 2, p0.x, p0.y);

        ObjectAnimator move0 = ObjectAnimator.ofFloat(rocketImageView, View.X, View.Y, path0);
        move0.setDuration(edgeDuration);
        move0.setInterpolator(new LinearInterpolator()); 

        ObjectAnimator move1 = ObjectAnimator.ofFloat(rocketImageView, View.X, View.Y, path1);
        move1.setDuration(edgeDuration);
        move1.setInterpolator(new LinearInterpolator());

        ObjectAnimator move2 = ObjectAnimator.ofFloat(rocketImageView, View.X, View.Y, path2);
        move2.setDuration(edgeDuration);
        move2.setInterpolator(new LinearInterpolator());

        ObjectAnimator move3 = ObjectAnimator.ofFloat(rocketImageView, View.X, View.Y, path3);
        move3.setDuration(edgeDuration);
        move3.setInterpolator(new LinearInterpolator());

        ObjectAnimator rotateTo180 = ObjectAnimator.ofFloat(rocketImageView, "rotation", 90, 180);
        rotateTo180.setDuration(rotationDuration);
        rotateTo180.setInterpolator(new LinearInterpolator());

        ObjectAnimator rotateTo270 = ObjectAnimator.ofFloat(rocketImageView, "rotation", 180, 270);
        rotateTo270.setDuration(rotationDuration);
        rotateTo270.setInterpolator(new LinearInterpolator());

        ObjectAnimator rotateTo360 = ObjectAnimator.ofFloat(rocketImageView, "rotation", 270, 360); 
        rotateTo360.setDuration(rotationDuration);
        rotateTo360.setInterpolator(new LinearInterpolator());
        
        ObjectAnimator rotateTo450 = ObjectAnimator.ofFloat(rocketImageView, "rotation", 360, 450); 
        rotateTo450.setDuration(rotationDuration);
        rotateTo450.setInterpolator(new LinearInterpolator());
        rotateTo450.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (rocketImageView != null) {
                    rocketImageView.setRotation(90); 
                }
            }
        });


        AnimatorSet sequentialAnimations = new AnimatorSet();
        sequentialAnimations.playSequentially(
                move0, rotateTo180, move1, rotateTo270,
                move2, rotateTo360, move3, rotateTo450
        );

        final AnimatorSet mainAnimatorSetInstance = new AnimatorSet();
        mainAnimatorSetInstance.play(sequentialAnimations);
        mainAnimatorSetInstance.setStartDelay(startDelay);
        mainAnimatorSetInstance.addListener(new AnimatorListenerAdapter() {
            private boolean wasCancelled = false;
            @Override
            public void onAnimationEnd(Animator animationArgument) { 
                super.onAnimationEnd(animationArgument);
                 
                if (Rocket.this.currentAnimatorSet != mainAnimatorSetInstance || wasCancelled) {
                    Log.d(TAG, "onAnimationEnd: Stale or cancelled animation ended. Current: " + Rocket.this.currentAnimatorSet + ", Ended: " + mainAnimatorSetInstance);
                    return; 
                }

                if (shouldBeAnimating && rocketImageView != null && rocketImageView.getVisibility() == View.VISIBLE) {
                    Log.d(TAG, "onAnimationEnd: Looping rocket animation with new stochastic path.");
                    rocketImageView.setRotation(90); 
                    animatePerimeterInternal(0); 
                } else {
                    Log.d(TAG, "onAnimationEnd: Not looping. shouldBeAnimating=" + shouldBeAnimating);
                    Rocket.this.currentAnimatorSet = null; 
                }
            }

            @Override
            public void onAnimationCancel(Animator animationArgument) {
                super.onAnimationCancel(animationArgument);
                wasCancelled = true; 
                Log.d(TAG, "Rocket animation cancelled for: " + (rocketImageView != null ? rocketImageView.getId() : "null_image_view") + ", Instance: " + mainAnimatorSetInstance);
                if (Rocket.this.currentAnimatorSet == mainAnimatorSetInstance) {
                    Rocket.this.currentAnimatorSet = null;
                }
            }
        });
        
        Log.d(TAG, "Starting new rocket animation. Instance: " + mainAnimatorSetInstance);
        mainAnimatorSetInstance.start();
        this.currentAnimatorSet = mainAnimatorSetInstance;
    }

    public void stopAnimation() {
        Log.d(TAG, "stopAnimation called externally.");
        this.shouldBeAnimating = false; 
        cancelAnimationInternal();
         if (rocketImageView != null) {
            rocketImageView.setVisibility(View.GONE); 
        }
    }
    
    private void cancelAnimationInternal() {
        if (currentAnimatorSet != null && currentAnimatorSet.isRunning()) {
            Log.d(TAG, "cancelAnimationInternal: Cancelling currentAnimatorSet. Instance: " + currentAnimatorSet);
            currentAnimatorSet.removeAllListeners();
            currentAnimatorSet.cancel();
        }
        currentAnimatorSet = null; 
    }

    public void setVisibility(int visibility) {
        if (rocketImageView != null) {
            rocketImageView.setVisibility(visibility);
            if (visibility == View.GONE || visibility == View.INVISIBLE) {
                if (shouldBeAnimating) { 
                    Log.d(TAG, "setVisibility: View set to GONE/INVISIBLE, ensuring animation is stopped.");
                    this.shouldBeAnimating = false; 
                    cancelAnimationInternal(); 
                }
            } else if (visibility == View.VISIBLE && shouldBeAnimating && (currentAnimatorSet == null || !currentAnimatorSet.isRunning())) {
                
                Log.d(TAG, "setVisibility: View set to VISIBLE and shouldBeAnimating is true, ensuring animation (re)starts.");
                updateAnimationStatusInternal(); 
            }
        }
    }

    public boolean isAnimating() {
        return currentAnimatorSet != null && currentAnimatorSet.isRunning();
    }
}
