package com.jasonstudio.cookbook2.Network;

import org.json.JSONException;
import org.json.JSONObject;

public interface CallbackListener {
    void getResult(JSONObject jsonObject) throws JSONException;
}
