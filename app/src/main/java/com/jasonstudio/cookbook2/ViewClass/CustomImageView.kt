package com.jasonstudio.cookbook2.ViewClass

import androidx.appcompat.widget.AppCompatImageView
import android.animation.ValueAnimator
import android.view.animation.ScaleAnimation
import com.jasonstudio.cookbook2.R
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.animation.Animation

class CustomImageView : AppCompatImageView {
    private lateinit var animationDown: Animation
    private lateinit var animationUp: Animation
    private var colorStart = 0
    private var colorEnd = 0
    private var duration = 0
    private lateinit var colorDownAnimator: ValueAnimator
    private lateinit var colorUpAnimator: ValueAnimator
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        duration = 200
        animationDown = ScaleAnimation(
            1f,
            0.8f,
            1f,
            0.8f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        animationDown.setDuration(duration.toLong())
        animationDown.setFillAfter(true)
        animationUp = ScaleAnimation(
            0.8f,
            1f,
            0.8f,
            1f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        animationUp.setDuration(duration.toLong())
        animationUp.setFillAfter(true)
        colorEnd = resources.getColor(R.color.colorActionDown)
        colorStart = resources.getColor(R.color.colorActionUp)
        colorDownAnimator = ValueAnimator.ofArgb(colorStart, colorEnd)
        colorDownAnimator.setDuration(duration.toLong())
        colorDownAnimator.addUpdateListener(AnimatorUpdateListener { animation -> //Log.d("onAnimationUpdate", animation.getAnimatedValue().toString());
            this@CustomImageView.setColorFilter(animation.animatedValue as Int)
        })
        colorUpAnimator = ValueAnimator.ofArgb(colorEnd, colorStart)
        colorUpAnimator.setDuration(duration.toLong())
        colorUpAnimator.addUpdateListener(AnimatorUpdateListener { animation ->
            this@CustomImageView.setColorFilter(
                animation.animatedValue as Int
            )
        })
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        super.onTouchEvent(event)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

                //this.getDrawable().setColorFilter(0x12000000, PorterDuff.Mode.SRC_ATOP);
                //this.invalidate();
                startAnimation(animationDown)
                colorDownAnimator!!.start()
            }
            MotionEvent.ACTION_UP -> {
                //this.getDrawable().clearColorFilter();
                //
                // this.invalidate();
                colorUpAnimator!!.start()
                startAnimation(animationUp)
            }
        }
        return true
    }
}