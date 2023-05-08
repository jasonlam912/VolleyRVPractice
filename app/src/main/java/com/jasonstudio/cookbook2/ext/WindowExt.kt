package com.jasonstudio.cookbook2.ext

import android.os.Build
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager

fun Window.hideStatusBar() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        this.insetsController?.hide(WindowInsets.Type.statusBars())
    } else {
        this.addFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }
}

fun Window.showStatusBar() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        this.insetsController?.show(WindowInsets.Type.statusBars())
    } else {
        this.clearFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }
}