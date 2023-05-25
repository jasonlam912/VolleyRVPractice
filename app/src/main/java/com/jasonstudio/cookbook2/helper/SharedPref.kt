package com.jasonstudio.cookbook2.helper

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.jasonstudio.cookbook2.Network.SpoonacularService
import javax.crypto.spec.IvParameterSpec

class SharedPref {
    companion object {
        private val API_KEY_INDEX = "API_KEY_INDEX"
        private val ENCRYPTED_KEY = "ENCRYPTED_KEY"
        private val IV_KEY = "IV_KEY"
        private
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
        fun getEncryptedData(): ByteArray {
            val str = instance.getString(ENCRYPTED_KEY, "")!!
            return str.toByteArray(Charsets.ISO_8859_1)
        }

        fun setEncryptedData(data: ByteArray) {
            val str = data.toString(Charsets.ISO_8859_1)
            instance.edit().apply {
                putString(ENCRYPTED_KEY, str)
                apply()
            }
        }
        fun getIv(): IvParameterSpec {
            val str = instance.getString(IV_KEY, "")!!
            return IvParameterSpec(str.toByteArray(Charsets.ISO_8859_1))
        }

        fun setIv(iv: IvParameterSpec) {
            val str = iv.iv.toString(Charsets.ISO_8859_1)
            instance.edit().apply {
                putString(IV_KEY, str)
                apply()
            }
        }
    }
}