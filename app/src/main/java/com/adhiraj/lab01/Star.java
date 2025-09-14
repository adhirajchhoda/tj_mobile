package com.adhiraj.lab01;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Star {
    private static final String TAG = "StarClass";
    private Context context;
    private ConstraintLayout mainLayout;
    private List<View> starViews = new ArrayList<>();
    private Random random = new Random();
    private int screenWidth;
    private int screenHeight;
    private boolean areStarsCurrentlyVisible = true; 

    public Star(Context context, ConstraintLayout mainLayout, int screenWidth, int screenHeight) {
        this.context = context;
        this.mainLayout = mainLayout;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        Log.d(TAG, "Star manager initialized with screenWidth: " + screenWidth + ", screenHeight: " + screenHeight);
    }

    public List<View> getStarViews() {
        return starViews;
    }

    public void setStarsVisible(boolean visible) {
        Log.d(TAG, "setStarsVisible called with: " + visible + ". Current state: " + areStarsCurrentlyVisible);
        if (this.areStarsCurrentlyVisible == visible && (visible && !starViews.isEmpty() || !visible && starViews.isEmpty())) {
            if (visible) {
                manageAnimations();
            }
            return;
        }
        this.areStarsCurrentlyVisible = visible;

        if (areStarsCurrentlyVisible) {
            if (screenWidth > 0 && screenHeight > 0) {
                Log.d(TAG, "Stars set to visible, ensuring they are created and animated.");
                if (starViews.isEmpty()) {
                     createInitialStars(); 
                } else {
                    for (View starView : starViews) {
                        starView.setVisibility(View.VISIBLE);
                        
                        AnimatorSet currentAnimatorSet = (AnimatorSet) starView.getTag(R.id.star_animator_set);
                        if (currentAnimatorSet == null || !currentAnimatorSet.isRunning()) {
                            resetStarPositionAndAnimate(starView);
                        }
                    }
                }
            } else {
                 Log.d(TAG, "Screen dimensions not set, cannot create or animate stars yet.");
            }
        } else {
            Log.d(TAG, "Stars set to hidden, removing all stars and animations.");
            for (View starView : starViews) {
                AnimatorSet animatorSet = (AnimatorSet) starView.getTag(R.id.star_animator_set); 
                if (animatorSet != null) {
                    animatorSet.cancel();
                    starView.setTag(R.id.star_animator_set, null); 
                }
                mainLayout.removeView(starView);
            }
            starViews.clear();
        }
    }

    public void updateScreenDimensions(int width, int height) {
        boolean dimensionsChanged = (this.screenWidth != width || this.screenHeight != height);
        this.screenWidth = width;
        this.screenHeight = height;
        Log.d(TAG, "Screen dimensions updated: width=" + width + ", height=" + height + ". Dimensions changed: " + dimensionsChanged);
        
        if (areStarsCurrentlyVisible) {
            if (screenWidth > 0 && screenHeight > 0) {
                if (starViews.isEmpty()) {
                    Log.d(TAG, "Screen dimensions set, stars are visible and none exist. Creating stars.");
                    createInitialStars();
                } else if (dimensionsChanged) { 
                    Log.d(TAG, "Dimensions changed, re-managing star animations for existing stars.");
                     for (View starView : starViews) {
                        starView.setVisibility(View.VISIBLE); 
                         resetStarPositionAndAnimate(starView); 
                    }
                }
                
                
                manageAnimations();
            } else {
                 Log.d(TAG, "Screen dimensions are zero, cannot manage stars.");
            }
        }
    }

    private void createInitialStars() {
        if (!areStarsCurrentlyVisible || screenWidth == 0 || screenHeight == 0) {
            Log.d(TAG, "Cannot create stars: areStarsCurrentlyVisible=" + areStarsCurrentlyVisible + ", screenWidth=" + screenWidth + ", screenHeight=" + screenHeight);
            return;
        }
        for (View starView : starViews) {
            AnimatorSet animatorSet = (AnimatorSet) starView.getTag(R.id.star_animator_set);
            if (animatorSet != null) animatorSet.cancel();
            mainLayout.removeView(starView);
        }
        starViews.clear();

        int numberOfStars = 15;
        Log.d(TAG, "Creating " + numberOfStars + " stars.");
        for (int i = 0; i < numberOfStars; i++) {
            ImageView starImageView = createStarImageViewInstance();
            starViews.add(starImageView);
            mainLayout.addView(starImageView);
            resetStarPositionAndAnimate(starImageView);
        }
    }

    private ImageView createStarImageViewInstance() {
        ImageView star = new ImageView(context);
        star.setImageResource(R.drawable.star); 
        int starSize = random.nextInt(30) + 20; 
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(starSize, starSize);
        star.setLayoutParams(params);
        star.setVisibility(areStarsCurrentlyVisible ? View.VISIBLE : View.GONE);
        return star;
    }

    private void resetStarPositionAndAnimate(View starView) {
        if (!areStarsCurrentlyVisible || screenWidth == 0 || screenHeight == 0) {
            starView.setVisibility(View.GONE);
            AnimatorSet animatorSet = (AnimatorSet) starView.getTag(R.id.star_animator_set);
            if (animatorSet != null) animatorSet.cancel();
            Log.d(TAG, "resetStarPositionAndAnimate: Conditions not met, hiding star " + starView.hashCode());
            return;
        }
        starView.setVisibility(View.VISIBLE);

        
        AnimatorSet oldAnimatorSet = (AnimatorSet) starView.getTag(R.id.star_animator_set);
        if (oldAnimatorSet != null) {
            oldAnimatorSet.cancel(); 
            starView.setTag(R.id.star_animator_set, null); 
        }

        int starViewWidth = starView.getWidth() > 0 ? starView.getWidth() : ((ConstraintLayout.LayoutParams)starView.getLayoutParams()).width;
        int starViewHeight = starView.getHeight() > 0 ? starView.getHeight() : ((ConstraintLayout.LayoutParams)starView.getLayoutParams()).height;

        if (starViewWidth <=0 || starViewHeight <=0) {
            Log.w(TAG, "Star view dimensions are zero, star: " + starView.hashCode() + ". Using default size 20x20 for calculation.");
            starViewWidth = Math.max(starViewWidth, 20); 
            starViewHeight = Math.max(starViewHeight, 20); 
        }
        
        starView.setX(random.nextInt(Math.max(1, screenWidth - starViewWidth)));
        starView.setY(random.nextInt(Math.max(1, screenHeight - starViewHeight)));
        starView.setRotation(random.nextFloat() * 360); 
        Log.d(TAG, "Resetting star " + starView.hashCode() + " to X:" + starView.getX() + " Y:" + starView.getY() + " R:" + starView.getRotation() + " W:" + starViewWidth + " H:" + starViewHeight + " screenW:" + screenWidth + " screenH:" + screenHeight);

        int targetX = random.nextInt(Math.max(1, screenWidth - starViewWidth));
        int targetY = random.nextInt(Math.max(1, screenHeight - starViewHeight));
        float targetRotation = starView.getRotation() + (random.nextBoolean() ? 360 : -360) + (random.nextFloat() * 180 - 90); 
        long duration = random.nextInt(5000) + 3000;

        ObjectAnimator animX = ObjectAnimator.ofFloat(starView, "x", starView.getX(), targetX);
        ObjectAnimator animY = ObjectAnimator.ofFloat(starView, "y", starView.getY(), targetY);
        ObjectAnimator animRotate = ObjectAnimator.ofFloat(starView, "rotation", starView.getRotation(), targetRotation);
        
        AnimatorSet newAnimatorSet = new AnimatorSet();
        newAnimatorSet.playTogether(animX, animY, animRotate);
        newAnimatorSet.setDuration(duration);
        newAnimatorSet.setInterpolator(new LinearInterpolator());
        
        starView.setTag(R.id.star_animator_set, newAnimatorSet);

        newAnimatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                
                if (starView.getTag(R.id.star_animator_set) == newAnimatorSet) { 
                    if (areStarsCurrentlyVisible && starView.getVisibility() == View.VISIBLE && mainLayout.indexOfChild(starView) != -1) {
                        Log.d(TAG, "Star animation ended, resetting position for star: " + starView.hashCode());
                        resetStarPositionAndAnimate(starView);
                    } else {
                        Log.d(TAG, "Star animation ended but star no longer needs reset or is removed: " + starView.hashCode());
                         if (mainLayout.indexOfChild(starView) == -1) {
                             starViews.remove(starView); 
                         }
                         starView.setTag(R.id.star_animator_set, null); 
                    }
                } else {
                     Log.d(TAG, "Star animation ended (possibly old one) for star: " + starView.hashCode());
                }
            }
             @Override
            public void onAnimationCancel(Animator animation) {
                Log.d(TAG, "Star animation cancelled for star: " + starView.hashCode());
                
                if (starView.getTag(R.id.star_animator_set) == newAnimatorSet) {
                    starView.setTag(R.id.star_animator_set, null);
                }
            }
        });
        newAnimatorSet.start();
    }

    public void manageAnimations() {
        Log.d(TAG, "manageAnimations called. areStarsCurrentlyVisible: " + areStarsCurrentlyVisible);
        if (!areStarsCurrentlyVisible) {
            Log.d(TAG, "Stars not visible, ensuring all animations are cancelled and views removed.");
            for (View starView : starViews) {
                AnimatorSet animatorSet = (AnimatorSet) starView.getTag(R.id.star_animator_set);
                if (animatorSet != null) {
                    animatorSet.cancel();
                    starView.setTag(R.id.star_animator_set, null); 
                }
                mainLayout.removeView(starView);
            }
            starViews.clear();
            return;
        }

        if (starViews.isEmpty() && screenWidth > 0 && screenHeight > 0) {
            Log.d(TAG, "No stars exist but should be visible, creating initial set via manageAnimations.");
            createInitialStars();
        } else {
            for (View starView : new ArrayList<>(starViews)) { 
                if (mainLayout.indexOfChild(starView) == -1) {
                    Log.d(TAG, "Star " + starView.hashCode() + " not in layout, removing from list.");
                    starViews.remove(starView); 
                    continue;
                }
                starView.setVisibility(View.VISIBLE); 
                AnimatorSet animatorSet = (AnimatorSet) starView.getTag(R.id.star_animator_set);
                if (animatorSet == null || !animatorSet.isRunning()) {
                    Log.d(TAG, "Star " + starView.hashCode() + " animation null or ended/not running, resetting in manageAnimations.");
                    resetStarPositionAndAnimate(starView);
                }
            }
        }
    }
}
