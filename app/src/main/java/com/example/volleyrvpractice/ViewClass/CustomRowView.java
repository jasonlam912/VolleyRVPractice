package com.example.volleyrvpractice.ViewClass;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.volleyrvpractice.R;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_UP;
import static android.view.MotionEvent.AXIS_SIZE;

public class CustomRowView extends ConstraintLayout {

    private int duration = 100;

    private Animation animationDown;
    private Animation animationUp;

    private ValueAnimator colorDownAnimator;
    private ValueAnimator colorUpAnimator;

    private int colorStart;
    private int colorEnd;

    private boolean press = false;

    public CustomRowView(Context context) {
        super(context);
    }

    public CustomRowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        colorStart = getResources().getColor(R.color.colorActionUp);
        colorEnd = getResources().getColor(R.color.colorActionDown);

        animationDown = new ScaleAnimation(1f,0.8f,1f,0.8f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animationDown.setDuration(duration);
        animationDown.setFillAfter(true);
        animationUp = new ScaleAnimation(0.8f, 1f, 0.8f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animationUp.setDuration(duration);
        animationUp.setFillAfter(true);

        colorDownAnimator = ValueAnimator.ofArgb(colorStart, colorEnd);
        colorDownAnimator.setDuration(duration);
        colorDownAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                CustomRowView.this.setBackgroundColor((int)animation.getAnimatedValue());
            }
        });

        colorUpAnimator = ValueAnimator.ofArgb(colorEnd, colorStart);
        colorUpAnimator.setDuration(duration);
        colorUpAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                CustomRowView.this.setBackgroundColor((int) animation.getAnimatedValue());
            }
        });
    }

    public CustomRowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()){
            case ACTION_DOWN:{
                this.startAnimation(animationDown);
                colorDownAnimator.start();
                break;
            }
            case ACTION_UP:
        case AXIS_SIZE: {
            this.startAnimation(animationUp);
            colorUpAnimator.start();
            break;
        }
    }
        //Log.d("getAction",Integer.toString(event.getAction()));
        return true;
    }
}
