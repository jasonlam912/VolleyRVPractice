package com.jasonstudio.cookbook2.Network

import android.graphics.Bitmap

interface GlideCallbackListener {
    fun getBitmap(resource: Bitmap, index: Int)
}