package com.jasonstudio.cookbook2.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.ScaleAnimation

class ShadowView : View {
    private lateinit var animation1: Animation
    private lateinit var animation2: Animation
    private lateinit var animationalpha1: Animation
    private lateinit var animationalpha2: Animation
    private lateinit var animSet1: AnimationSet
    private lateinit var animSet2: AnimationSet

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        animation1 = ScaleAnimation(
            1f,
            1.2f,
            1f,
            1.2f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        animation1.duration = 600
        animation2 = ScaleAnimation(
            1.2f,
            1f,
            1.2f,
            1f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        animation2.duration = 600
        animationalpha1 = AlphaAnimation(1f, 0.35f)
        animationalpha1.duration = 600
        animationalpha2 = AlphaAnimation(0.35f, 1f)
        animationalpha2.duration = 600
        animSet1 = AnimationSet(true)
        animSet2 = AnimationSet(true)
        animSet1.addAnimation(animationalpha1)
        animSet1.addAnimation(animation1)
        animSet2.addAnimation(animation2)
        animSet2.addAnimation(animationalpha2)
        animSet1.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                startAnimation(animSet2)
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        animSet2.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                startAnimation(animSet1)
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        startAnimation(animSet1)
    }
}