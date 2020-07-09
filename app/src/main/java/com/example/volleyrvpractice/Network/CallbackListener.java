package com.example.volleyrvpractice.Network;

import org.json.JSONException;
import org.json.JSONObject;

public interface CallbackListener {
    void getResult(JSONObject jsonObject) throws JSONException;
}
