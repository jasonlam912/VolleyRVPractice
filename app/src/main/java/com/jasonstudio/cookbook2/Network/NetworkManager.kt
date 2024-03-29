package com.jasonstudio.cookbook2.Network

import android.content.Context
import android.graphics.Bitmap
import android.util.LruCache
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.jasonstudio.cookbook2.R
import org.json.JSONException

class NetworkManager private constructor(val ctx: Context) {
    private var requestQueue: RequestQueue?
    val imageLoader: ImageLoader

    init {
        api = ctx.resources.getString(R.string.apiKeyUsing)
        requestQueue = getRequestQueue()
        imageLoader = ImageLoader(requestQueue,
            object : ImageLoader.ImageCache {
                private val cache = LruCache<String, Bitmap>(20)
                override fun getBitmap(url: String): Bitmap? {
                    return cache[url]
                }

                override fun putBitmap(url: String, bitmap: Bitmap) {
                    cache.put(url, bitmap)
                }
            })
    }

    fun getRequestQueue(): RequestQueue? {
        if (requestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            requestQueue = Volley.newRequestQueue(ctx.applicationContext)
        }
        return requestQueue
    }

    fun <T> addToRequestQueue(req: Request<T>?) {
        getRequestQueue()!!.add(req)
    }

    fun getSearchRecipe(query: String, offset: Int, listener: CallbackListener) {
        val request = JsonObjectRequest(
            Request.Method.GET,
            searchRecipeUrl1 + api + searchRecipeUrl2 + query + searchRecipeUrl3 + offset,
            null,
            { response ->
                try {
                    listener.getResult(response)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }) { error -> error.printStackTrace() }
        requestQueue!!.add(request)
    }

    companion object {
        private var instance: NetworkManager? = null
        private const val randomRecipeUrl =
            "https://api.spoonacular.com/recipes/random?number=10&apiKey="
        private const val searchRecipeUrl1 =
            "https://api.spoonacular.com/recipes/complexSearch?number=10&apiKey="
        private lateinit var api: String
        private const val searchRecipeUrl2 = "&query="
        private const val searchRecipeUrl3 = "&offset="
        @JvmStatic
        @Synchronized
        fun getInstance(context: Context): NetworkManager {
            if (instance == null) {
                instance = NetworkManager(context)
                //Log.d("getInstance",instance.toString());
            }
            return instance!!
        }
    }
}