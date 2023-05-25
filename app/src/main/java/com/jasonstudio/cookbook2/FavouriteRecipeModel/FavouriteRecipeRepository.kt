package com.jasonstudio.cookbook2.FavouriteRecipeModel

import android.app.Application
import io.reactivex.Observable

class FavouriteRecipeRepository(application: Application?) {
    private var fRDao: FavouriteRecipeDao
    val allFR: Observable<List<FavouriteRecipe>>

    init {
        val db: FavouriteRecipeDatabase = FavouriteRecipeDatabase.getInstance(application)
        fRDao = db.favouriteRecipeDao()
        allFR = fRDao.allFR
    }

    suspend fun delete(favouriteRecipe: FavouriteRecipe?) {
        fRDao.deleteFR(favouriteRecipe)
    }

    suspend fun insert(favouriteRecipe: FavouriteRecipe?) {
        fRDao.insertFR(favouriteRecipe)
    }

    suspend fun deleteAllFR() {
        fRDao.deleteAllFR()
    }

    suspend fun deleteWithKey(primaryKey: Int) {
        fRDao.deleteFRwithKey(primaryKey)
    }
}