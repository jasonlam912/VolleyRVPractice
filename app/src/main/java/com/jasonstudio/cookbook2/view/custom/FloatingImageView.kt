package com.jasonstudio.cookbook2.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.animation.Animation
import androidx.appcompat.widget.AppCompatImageView
import android.view.animation.AnimationSet
import android.view.animation.TranslateAnimation
import android.view.animation.ScaleAnimation

class FloatingImageView : AppCompatImageView {
    private lateinit var animation1: Animation
    private lateinit var animation2: Animation
    private lateinit var animation3: Animation
    private lateinit var animation4: Animation
    private lateinit var animSet1: AnimationSet
    private lateinit var animSet2: AnimationSet

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        animation1 = TranslateAnimation(0f, 0f, 0f, -50f)
        animation1.setDuration(600)
        animation1.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                startAnimation(animSet2)
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        animation2 = TranslateAnimation(0f, 0f, -50f, 0f)
        animation2.setDuration(600)
        animation2.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                startAnimation(animSet1)
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        animation3 = ScaleAnimation(
            1f, 0.95f, 1f, 0.95f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
        )
        animation3.setDuration(600)
        animation4 = ScaleAnimation(
            0.95f, 1f, 0.95f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
        )
        animation4.setDuration(600)
        animSet1 = AnimationSet(true)
        animSet1!!.addAnimation(animation1)
        animSet1!!.addAnimation(animation4)
        animSet2 = AnimationSet(true)
        animSet2!!.addAnimation(animation2)
        animSet2!!.addAnimation(animation3)
        startAnimation(animSet1)
    }
}