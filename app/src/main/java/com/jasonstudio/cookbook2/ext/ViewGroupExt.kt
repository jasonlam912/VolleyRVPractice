package com.jasonstudio.cookbook2.ext

import android.animation.TypeEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import com.jasonstudio.cookbook2.util.LogUtil

fun ViewGroup.valueAnimate(
    duration: Long,
    fromValue: AnimProperty,
    toValue: AnimProperty,
) {
    for (value in listOf(fromValue, toValue)) {
        value.width = if (value.width == -1) (this.parent as View).measuredWidth else value.width.toPx(context)
        value.height = if (value.height == -1) (this.parent as View).measuredHeight else value.height.toPx(context)
        value.marginTop = value.marginTop.toPx(context)
        value.marginBottom = value.marginBottom.toPx(context)
        value.marginStart = value.marginStart.toPx(context)
        value.marginEnd = value.marginEnd.toPx(context)
    }
    val anim = ValueAnimator.ofObject(AnimPropertyEvaluator(), fromValue, toValue)
    anim.addUpdateListener { valueAnimator ->
        val value = valueAnimator.animatedValue as AnimProperty
        val layoutParams: ViewGroup.MarginLayoutParams = this.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.topMargin = value.marginTop
        layoutParams.bottomMargin = value.marginBottom
        layoutParams.marginStart = value.marginStart
        layoutParams.marginEnd = value.marginEnd
        layoutParams.width = value.width
        layoutParams.height = value.height
        this.layoutParams = layoutParams
    }
    anim.duration = duration
    anim.start()
}


data class AnimProperty(
    var marginTop: Int,
    var marginBottom: Int,
    var marginStart: Int,
    var marginEnd: Int,
    var width: Int,
    var height: Int,
)

class AnimPropertyEvaluator: TypeEvaluator<AnimProperty> {
    override fun evaluate(p0: Float, p1: AnimProperty, p2: AnimProperty): AnimProperty {
        val value = DecelerateInterpolator().getInterpolation(p0)
        val res = AnimProperty(
            (p1.marginTop * (1 - value) + p2.marginTop * value).toInt(),
            (p1.marginBottom * (1 - value) + p2.marginBottom * value).toInt(),
            (p1.marginStart * (1 - value) + p2.marginStart * value).toInt(),
            (p1.marginEnd * (1 - value) + p2.marginEnd * value).toInt(),
            (p1.width * (1 - value) + p2.width * value).toInt(),
            (p1.height * (1 - value) + p2.height * value).toInt(),
        )
        LogUtil.log(res)
        return res
    }

}