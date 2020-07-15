package com.example.volleyrvpractice.FavouriteRecipeModel;

import android.app.Application;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class FavouriteRecipeViewModel extends AndroidViewModel {
    private FavouriteRecipeRepository repository;
    private LiveData<List<FavouriteRecipe>> allList;

    public FavouriteRecipeViewModel(@NonNull Application application) {
        super(application);
        repository = new FavouriteRecipeRepository(application);
        allList = repository.getAllFR();
    }

    public void insert(FavouriteRecipe recipe){
        repository.insert(recipe);
    }
    public void delete(FavouriteRecipe recipe){
        repository.delete(recipe);
    }
    public void deleteAll(){
        repository.deleteAllFR();
    }
    public LiveData<List<FavouriteRecipe>> getAll(){
        return allList;
    }

}
