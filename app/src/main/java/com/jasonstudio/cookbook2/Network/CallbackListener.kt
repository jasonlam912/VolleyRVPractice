package com.jasonstudio.cookbook2.Network

import org.json.JSONObject

interface CallbackListener {
    fun getResult(jsonObject: JSONObject)
}