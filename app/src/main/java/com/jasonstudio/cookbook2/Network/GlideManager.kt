package com.example.volleyrvpractice.Network

import android.content.Context
import com.bumptech.glide.request.target.CustomTarget
import android.graphics.Bitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import android.graphics.drawable.Drawable
import com.example.volleyrvpractice.R
import com.android.volley.toolbox.Volley
import com.example.volleyrvpractice.Network.CallbackListener
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject
import org.json.JSONException
import com.android.volley.VolleyError
import com.bumptech.glide.request.transition.Transition
import kotlin.Throws

object GlideManager {
    fun loadImage(
        ct: Context?,
        url: String?,
        i: Int,
        listener: GlideCallbackListener
    ): CustomTarget<Bitmap> {
        return Glide.with(ct!!).asBitmap().load(url).diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(object : CustomTarget<Bitmap>(556, 370) {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    //Log.d("onResourceReady",resource.toString();
                    listener.getBitmap(resource, i)
                }

                override fun onLoadCleared(placeholder: Drawable?) {}
            })
    }
}