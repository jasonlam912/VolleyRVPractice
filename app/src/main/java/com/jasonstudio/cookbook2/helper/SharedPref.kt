package com.jasonstudio.cookbook2.helper

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.jasonstudio.cookbook2.Network.SpoonacularService

class SharedPref {
    companion object {
        private val API_KEY_INDEX = "API_KEY_INDEX"
        lateinit var instance: SharedPreferences
        fun init(ctx: Context) {
            instance = ctx.getSharedPreferences(ctx.packageName, Activity.MODE_PRIVATE)
        }
        fun getApiKeyIndex(): Int {
            return instance.getInt(API_KEY_INDEX, 0)
        }
        @SuppressLint("ApplySharedPref")
        fun addApiKeyIndex() {
            val count = SpoonacularService.apiKeys.size
            instance.edit().apply {
                putInt(API_KEY_INDEX, (getApiKeyIndex() + 1) % count)
                commit()
            }
        }


    }
}