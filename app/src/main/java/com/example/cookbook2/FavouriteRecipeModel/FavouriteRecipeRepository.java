package com.example.cookbook2.FavouriteRecipeModel;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import io.reactivex.Observable;

public class FavouriteRecipeRepository {
    private FavouriteRecipeDao fRDao;
    private Observable<List<FavouriteRecipe>> allFR;

    public FavouriteRecipeRepository(Application application) {
        FavouriteRecipeDatabase db = FavouriteRecipeDatabase.getInstance(application);
        fRDao = db.favouriteRecipeDao();
        allFR = fRDao.getAllFR();
    }

    public void delete(FavouriteRecipe favouriteRecipe){
        new DeleteFRAsyncTasks(fRDao).execute(favouriteRecipe);
    }
    public void insert(FavouriteRecipe favouriteRecipe){
        new InsertFRAsyncTasks(fRDao).execute(favouriteRecipe);
    }
    public Observable<List<FavouriteRecipe>> getAllFR(){
        return allFR;
    }
    public void deleteAllFR(){
        new DeleteAllFRsAsyncTasks(fRDao).execute();
    }

    public void deleteWithKey(int primaryKey){
        new DeleteFRWithKeyAsyncTasks(fRDao).execute(primaryKey);

    }

    private class DeleteFRWithKeyAsyncTasks extends AsyncTask<Integer, Void, Void>{
        private FavouriteRecipeDao fRDao;
        private DeleteFRWithKeyAsyncTasks(FavouriteRecipeDao fRDao) {
            this.fRDao = fRDao;
        }

        @Override
        protected Void doInBackground(Integer... key) {
            fRDao.deleteFRwithKey(key[0]);
            return null;
        }
    }

    private class DeleteFRAsyncTasks extends AsyncTask<FavouriteRecipe, Void, Void>{
        private FavouriteRecipeDao fRDao;
        private DeleteFRAsyncTasks(FavouriteRecipeDao fRDao) {
            this.fRDao = fRDao;
        }

        @Override
        protected Void doInBackground(FavouriteRecipe... favouriteRecipes) {
            fRDao.deleteFR(favouriteRecipes[0]);
            return null;
        }
    }
    private class InsertFRAsyncTasks extends AsyncTask<FavouriteRecipe, Void, Void>{
        private FavouriteRecipeDao fRDao;
        private InsertFRAsyncTasks(FavouriteRecipeDao fRDao) {
            this.fRDao = fRDao;
        }

        @Override
        protected Void doInBackground(FavouriteRecipe... favouriteRecipes) {
            fRDao.insertFR(favouriteRecipes[0]);
            return null;
        }
    }
    private class DeleteAllFRsAsyncTasks extends AsyncTask<Void, Void, Void>{
        private FavouriteRecipeDao fRDao;
        private DeleteAllFRsAsyncTasks(FavouriteRecipeDao fRDao) {
            this.fRDao = fRDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            fRDao.deleteAllFR();
            return null;
        }
    }

}
