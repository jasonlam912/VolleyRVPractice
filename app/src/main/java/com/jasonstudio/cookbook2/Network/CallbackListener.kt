package com.example.volleyrvpractice.Network

import com.example.volleyrvpractice.Network.GlideCallbackListener
import com.bumptech.glide.request.target.CustomTarget
import android.graphics.Bitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import android.graphics.drawable.Drawable
import com.android.volley.RequestQueue
import com.example.volleyrvpractice.Network.NetworkManager
import com.example.volleyrvpractice.R
import com.android.volley.toolbox.Volley
import com.example.volleyrvpractice.Network.CallbackListener
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject
import org.json.JSONException
import com.android.volley.VolleyError
import kotlin.Throws

interface CallbackListener {
    fun getResult(jsonObject: JSONObject)
}