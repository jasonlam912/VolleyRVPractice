package com.jasonstudio.cookbook2.Recipe;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.jasonstudio.cookbook2.FavouriteRecipeModel.FavouriteRecipe;
import com.jasonstudio.cookbook2.Network.CallbackListener;
import com.jasonstudio.cookbook2.Network.GlideCallbackListener;
import com.jasonstudio.cookbook2.Network.GlideManager;
import com.jasonstudio.cookbook2.Network.NetworkManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RecipeViewModel extends ViewModel {
    private MutableLiveData<List<RecipeModel>> data;
    private List<CustomTarget<Bitmap>> foodImageRequestList;
    public Boolean isSearch;
    public MutableLiveData<Boolean> isLoading;


    public RecipeViewModel() {
        this.data = new MutableLiveData<>();
        this.data.setValue(new ArrayList<RecipeModel>());
        this.isSearch = false;
        this.isLoading = new MutableLiveData<>();
        this.isLoading.setValue(false);
        this.foodImageRequestList = new ArrayList<>();
        addPaddingItem();
        addPaddingItem();
    }

    public void resetData(Context ct){
        this.data.setValue(new ArrayList<RecipeModel>());
        for(CustomTarget<Bitmap> request:foodImageRequestList){
            Glide.with(ct.getApplicationContext()).clear(request);
        };
        Log.d("imagerequestListNo.:", Integer.toString(foodImageRequestList.size()));
        foodImageRequestList.clear();
        addPaddingItem();
        addPaddingItem();
    }

    public MutableLiveData<List<RecipeModel>> getData(){
        return data;
    }

    public void putRandomRecipe(final Context ct){
        Log.d("size", Integer.toString(data.getValue().size()));
        isLoading.setValue(true);
        if(isSearch){
            resetData(ct);
            isSearch = !isSearch;
        }
        NetworkManager.getInstance(ct).getRandomRecipe(new CallbackListener() {
            @Override
            public void getResult(JSONObject jsonObject) throws JSONException {

                deleteLastItem();
                List<RecipeModel> list = data.getValue();
                //Log.d("isNull", Boolean.toString(allFRList.getValue()==null));
                List<RecipeModel> newList = JsonData2Recipe.jsonObject2RandomRecipe(jsonObject,new ArrayList<FavouriteRecipe>());
                list.addAll(newList);
                loadFoodImage(list, ct);
                data.setValue(list);
                if(newList.size()==0){
                    addEndItem();
                }else{
                    addLoadingItem();
                }
                isLoading.setValue(false);
            }
        });
    }

    public void putSearchRecipe(final Context ct, String query){
        isLoading.setValue(true);
        if(!isSearch){
            resetData(ct);
            isSearch = !isSearch;
        }
        NetworkManager.getInstance(ct).getSearchRecipe(query, data.getValue().size()-2, new CallbackListener() {
            @Override
            public void getResult(JSONObject jsonObject) throws JSONException {
                deleteLastItem();
                List<RecipeModel> list = data.getValue();
                List<RecipeModel> newList = JsonData2Recipe.jsonObject2SearchRecipe(jsonObject, new ArrayList<FavouriteRecipe>());
                list.addAll(newList);
                loadFoodImage(list, ct);
                data.setValue(list);
                if(newList.size()==0){
                    addEndItem();
                }else{
                    addLoadingItem();
                }
                isLoading.setValue(false);
            }
        });
    }

    public void putRecipe(Context ct, String query){
        if(isSearch){
            putSearchRecipe(ct, query);
        }else{
            putRandomRecipe(ct);
        }
    }

    public void addPaddingItem(){
        RecipeModel recipe = new RecipeModel(null,null,null,3, true, null);
        this.data.getValue().add(recipe);
    }

    public void deleteLastItem(){
        List<RecipeModel> list = this.data.getValue();
        list.remove(list.size()-1);
        this.data.setValue(list);
    }

    public void addLoadingItem(){
        List<RecipeModel> list = this.data.getValue();
        list.add(new RecipeModel(null,null,null,1, true, null));
        this.data.setValue(list);
    }

    public void addEndItem(){
        List<RecipeModel> list = this.data.getValue();
        list.add(new RecipeModel(null,null,null,2, true, null));
        this.data.setValue(list);
    }

    public void loadFoodImage(List<RecipeModel> list, final Context ct){
        for(int i=0; i<list.size();i++){
            //Log.d("boolean geresult", Boolean.toString(list.get(i).getStatusForDisplay().equals(0)&&list.get(i).getRecipeIamge()==null));
            if(list.get(i).getStatusForDisplay().equals(0)&&list.get(i).getRecipeIamge()==null){
                final int finalI = i;
                //Log.d("url",list.get(i).getRecipeImageUrl() );
                CustomTarget<Bitmap> request = GlideManager.loadImage(ct.getApplicationContext(), list.get(i).getRecipeImageUrl(), i, new GlideCallbackListener() {
                    @Override
                    public void getBitmap(Bitmap resource, int index) {
                        //Log.d("getBitmap", Boolean.toString(resource==null));

                        List<RecipeModel> tempList = data.getValue();
                        Bitmap temp = resource.copy(resource.getConfig(), true);
                        tempList.get(index).setRecipeImage(temp);
                        data.postValue(tempList);
                        //Glide.with(ct.getApplicationContext()).clear(request);
                    }

                });
                foodImageRequestList.add(request);
                //list.get(i).setRecipeImage(Glide.with(ct).asBitmap().load(list.get(i).getRecipeImageUrl()).submit(556,370).get());




            }
        }
    }
}
