package com.example.volleyrvpractice.ViewClass;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import androidx.appcompat.widget.AppCompatImageView;

import com.example.volleyrvpractice.R;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_UP;

public class CustomImageView extends AppCompatImageView {

    private Animation animationDown;
    private Animation animationUp;

    private int colorStart;
    private int colorEnd;

    private int duration;

    private ValueAnimator colorDownAnimator;
    private ValueAnimator colorUpAnimator;


    public CustomImageView(Context context) {
        super(context);
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        duration = 200;
        animationDown = new ScaleAnimation(1f,0.8f,1f,0.8f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animationDown.setDuration(duration);
        animationDown.setFillAfter(true);
        animationUp = new ScaleAnimation(0.8f, 1f, 0.8f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animationUp.setDuration(duration);
        animationUp.setFillAfter(true);
        colorEnd = getResources().getColor(R.color.colorActionDown);
        colorStart = getResources().getColor(R.color.colorActionUp);

        colorDownAnimator = ValueAnimator.ofArgb(colorStart,colorEnd);
        colorDownAnimator.setDuration(duration);
        colorDownAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //Log.d("onAnimationUpdate", animation.getAnimatedValue().toString());
                CustomImageView.this.setColorFilter((int) animation.getAnimatedValue());
            }
        });

        colorUpAnimator = ValueAnimator.ofArgb(colorEnd,colorStart);
        colorUpAnimator.setDuration(duration);
        colorUpAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                CustomImageView.this.setColorFilter((int) animation.getAnimatedValue());
            }
        });
    }

    public CustomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()){
            case ACTION_DOWN:{
                //this.getDrawable().setColorFilter(0x12000000, PorterDuff.Mode.SRC_ATOP);
                //this.invalidate();

                CustomImageView.this.startAnimation(animationDown);
                colorDownAnimator.start();
                break;
            }
            case ACTION_UP:
                //this.getDrawable().clearColorFilter();
                //
                // this.invalidate();
                colorUpAnimator.start();
                CustomImageView.this.startAnimation(animationUp);
                break;
        }
        return true;
    }
}
