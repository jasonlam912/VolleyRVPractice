package com.jasonstudio.cookbook2.Network

import com.jasonstudio.cookbook2.Network.GlideCallbackListener
import com.bumptech.glide.request.target.CustomTarget
import android.graphics.Bitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import android.graphics.drawable.Drawable
import com.android.volley.RequestQueue
import com.jasonstudio.cookbook2.Network.NetworkManager
import com.jasonstudio.cookbook2.R
import com.android.volley.toolbox.Volley
import com.jasonstudio.cookbook2.Network.CallbackListener
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject
import org.json.JSONException
import com.android.volley.VolleyError
import kotlin.Throws

interface GlideCallbackListener {
    fun getBitmap(resource: Bitmap, index: Int)
}