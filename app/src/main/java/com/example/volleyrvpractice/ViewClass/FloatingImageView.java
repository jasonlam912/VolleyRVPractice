package com.example.volleyrvpractice.ViewClass;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import androidx.annotation.Nullable;

public class FloatingImageView extends androidx.appcompat.widget.AppCompatImageView {

    private Animation animation1;
    private Animation animation2;
    private Animation animation3;
    private Animation animation4;

    private AnimationSet animSet1;
    private AnimationSet animSet2;


    public FloatingImageView(Context context) {
        super(context);
    }

    public FloatingImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FloatingImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        animation1 = new TranslateAnimation(0f, 0f, 0f,-50f);
        animation1.setDuration(600);
        animation1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                FloatingImageView.this.startAnimation(animSet2);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animation2 = new TranslateAnimation(0f,0f,-50f,0f);
        animation2.setDuration(600);
        animation2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                FloatingImageView.this.startAnimation(animSet1);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        animation3 = new ScaleAnimation(
                1f,0.95f,1f,0.95f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        animation3.setDuration(600);

        animation4 = new ScaleAnimation(
                0.95f,1f,0.95f,1f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        animation4.setDuration(600);

        animSet1 = new AnimationSet(true);
        animSet1.addAnimation(animation1);
        animSet1.addAnimation(animation4);
        animSet2 = new AnimationSet(true);
        animSet2.addAnimation(animation2);
        animSet2.addAnimation(animation3);
        FloatingImageView.this.startAnimation(animSet1);
    }
}
