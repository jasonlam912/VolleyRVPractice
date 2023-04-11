package com.jasonstudio.cookbook2.FavouriteRecipeModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.List;

import io.reactivex.Observable;

public class FavouriteRecipeViewModel extends AndroidViewModel {
    private FavouriteRecipeRepository repository;
    private Observable<List<FavouriteRecipe>> allList;

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

    public void deleteWithKey(int key) {
        repository.deleteWithKey(key);
    }

    public Observable<List<FavouriteRecipe>> getAll(){
        return allList;
    }


}
