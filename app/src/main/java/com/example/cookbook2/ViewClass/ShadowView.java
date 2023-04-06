package com.example.cookbook2.ViewClass;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;

import androidx.annotation.Nullable;

public class ShadowView extends View {

    private Animation animation1;
    private Animation animation2;
    private Animation animationalpha1;
    private Animation animationalpha2;
    private AnimationSet animSet1;
    private AnimationSet animSet2;


    public ShadowView(Context context) {
        super(context);
    }
    public ShadowView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    public ShadowView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public ShadowView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        animation1 = new ScaleAnimation(1f,1.2f,1f,1.2f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation1.setDuration(600);
        animation2 = new ScaleAnimation(1.2f,1f,1.2f,1f,Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        animation2.setDuration(600);
        animationalpha1 = new AlphaAnimation(1f,0.35f);
        animationalpha1.setDuration(600);
        animationalpha2 = new AlphaAnimation(0.35f,1f);
        animationalpha2.setDuration(600);
        animSet1 = new AnimationSet(true);
        animSet2 = new AnimationSet(true);
        animSet1.addAnimation(animationalpha1);
        animSet1.addAnimation(animation1);
        animSet2.addAnimation(animation2);
        animSet2.addAnimation(animationalpha2);
        animSet1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ShadowView.this.startAnimation(animSet2);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animSet2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ShadowView.this.startAnimation(animSet1);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        this.startAnimation(animSet1);
    }
}
