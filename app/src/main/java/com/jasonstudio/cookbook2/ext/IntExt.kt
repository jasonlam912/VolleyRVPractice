package com.jasonstudio.cookbook2.ext

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import kotlin.math.roundToInt

// Converts 14 dip into its equivalent px
fun Int.toPx(context: Context): Int {
    var r: Resources = context.resources
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), r.displayMetrics
    ).roundToInt()
}
