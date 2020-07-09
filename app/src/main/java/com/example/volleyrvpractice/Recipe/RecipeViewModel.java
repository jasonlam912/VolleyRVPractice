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
        addPaddingItem();
        addPaddingItem();
    }

    public void resetData(){
        this.data.setValue(new ArrayList<RecipeModel>());
        addPaddingItem();
        addPaddingItem();
    }

    public void putRandomRecipe(Context ct){
        NetworkManager.getInstance(ct).getRandomRecipe(new CallbackListener() {
            @Override
            public void getResult(JSONObject jsonObject) throws JSONException {
                deleteLastItem();
                List<RecipeModel> list = data.getValue();
                List<RecipeModel> newList = JsonData2Recipe.jsonObject2Recipe(jsonObject);
                list.addAll(newList);
                data.setValue(list);
                if(newList.size()==0){
                    addEndItem();
                }else{
                    addLoadingItem();
                }
            }
        });
    }

    public void putSearchRecipe(Context ct, String query, int offset){
        NetworkManager.getInstance(ct).getSearchRecipe(query, offset, new CallbackListener() {
            @Override
            public void getResult(JSONObject jsonObject) throws JSONException {
                deleteLastItem();
                List<RecipeModel> list = data.getValue();
                List<RecipeModel> newList = JsonData2Recipe.jsonObject2Recipe(jsonObject);
                list.addAll(newList);
                data.setValue(list);
                if(newList.size()==0){
                    addEndItem();
                }else{
                    addLoadingItem();
                }
            }
        });
    }

    public void addPaddingItem(){
        RecipeModel recipe = new RecipeModel(null,null,null,3, true);
        this.data.getValue().add(recipe);
    }

    public void deleteLastItem(){
        List<RecipeModel> list = this.data.getValue();
        list.remove(list.size()-1);
        this.data.setValue(list);
    }

    public void addLoadingItem(){
        List<RecipeModel> list = this.data.getValue();
        list.add(new RecipeModel(null,null,null,1, true));
        this.data.setValue(list);
    }

    public void addEndItem(){
        List<RecipeModel> list = this.data.getValue();
        list.add(new RecipeModel(null,null,null,2, true));
        this.data.setValue(list);
    }
}
