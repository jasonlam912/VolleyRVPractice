package com.jasonstudio.cookbook2.view.custom

import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.constraintlayout.widget.ConstraintLayout
import com.jasonstudio.cookbook2.R

class CustomRowView : ConstraintLayout {
    private val duration = 100
    private lateinit var animationDown: Animation
    private lateinit var animationUp: Animation
    private lateinit var colorDownAnimator: ValueAnimator
    private lateinit var colorUpAnimator: ValueAnimator
    private var colorStart = 0
    private var colorEnd = 0
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        colorStart = resources.getColor(R.color.colorActionUp)
        colorEnd = resources.getColor(R.color.colorActionDown)
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
        animationDown.duration = duration.toLong()
        animationDown.fillAfter = true
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
        animationUp.duration = duration.toLong()
        animationUp.fillAfter = true
        colorDownAnimator = ValueAnimator.ofArgb(colorStart, colorEnd)
        colorDownAnimator.duration = duration.toLong()
        colorDownAnimator.addUpdateListener(AnimatorUpdateListener { animation ->
            setBackgroundColor(
                animation.animatedValue as Int
            )
        })
        colorUpAnimator = ValueAnimator.ofArgb(colorEnd, colorStart)
        colorUpAnimator.duration = duration.toLong()
        colorUpAnimator.addUpdateListener(AnimatorUpdateListener { animation ->
            setBackgroundColor(
                animation.animatedValue as Int
            )
        })
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onTouchEvent(event: MotionEvent): Boolean {
        super.onTouchEvent(event)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startAnimation(animationDown)
                colorDownAnimator.start()
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                startAnimation(animationUp)
                colorUpAnimator.start()
            }
        }
        //Log.d("getAction",Integer.toString(event.getActionMasked()));
        return true
    }
}