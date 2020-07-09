package com.example.volleyrvpractice.Recipe;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.volleyrvpractice.Network.CallbackListener;
import com.example.volleyrvpractice.Network.NetworkManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RecipeViewModel extends ViewModel {
    private MutableLiveData<List<RecipeModel>> data;

    public RecipeViewModel() {
        this.data = new MutableLiveData<>();
        this.data.setValue(new ArrayList<RecipeModel>());
    }

    public void resetData(){
        this.data.setValue(new ArrayList<RecipeModel>());
    }

    public void putRandomRecipe(Context ct){
        NetworkManager.getInstance(ct).getRandomRecipe(new CallbackListener() {
            @Override
            public void getResult(JSONObject jsonObject) throws JSONException {
                 data.getValue().addAll(JsonData2Recipe.jsonObject2Recipe(jsonObject));
            }
        });
    }

    public void putSearchRecipe(Context ct, String query, int offset){
        NetworkManager.getInstance(ct).getSearchRecipe(query, offset, new CallbackListener() {
            @Override
            public void getResult(JSONObject jsonObject) throws JSONException {
                data.getValue().addAll(JsonData2Recipe.jsonObject2Recipe(jsonObject));
            }
        });
    }
}
