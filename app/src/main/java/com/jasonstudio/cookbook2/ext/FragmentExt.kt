package com.jasonstudio.cookbook2.ext

import android.os.Bundle
import androidx.fragment.app.Fragment

fun Fragment.upsertString(key: String, value: String) {
    val bundle = arguments ?: Bundle()
    bundle.putString(key, value)
    arguments = bundle
}