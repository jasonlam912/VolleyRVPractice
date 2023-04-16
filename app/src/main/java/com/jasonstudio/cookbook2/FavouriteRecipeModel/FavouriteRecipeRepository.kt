package com.jasonstudio.cookbook2.FavouriteRecipeModel

import android.app.Application
import androidx.room.PrimaryKey
import androidx.room.Dao
import androidx.room.OnConflictStrategy
import com.jasonstudio.cookbook2.FavouriteRecipeModel.FavouriteRecipe
import androidx.room.Database
import androidx.room.RoomDatabase
import com.jasonstudio.cookbook2.FavouriteRecipeModel.FavouriteRecipeDao
import com.jasonstudio.cookbook2.FavouriteRecipeModel.FavouriteRecipeDatabase
import androidx.room.Room
import androidx.lifecycle.AndroidViewModel
import com.jasonstudio.cookbook2.FavouriteRecipeModel.FavouriteRecipeRepository
import com.jasonstudio.cookbook2.FavouriteRecipeModel.FavouriteRecipeRepository.DeleteFRAsyncTasks
import com.jasonstudio.cookbook2.FavouriteRecipeModel.FavouriteRecipeRepository.InsertFRAsyncTasks
import com.jasonstudio.cookbook2.FavouriteRecipeModel.FavouriteRecipeRepository.DeleteAllFRsAsyncTasks
import com.jasonstudio.cookbook2.FavouriteRecipeModel.FavouriteRecipeRepository.DeleteFRWithKeyAsyncTasks
import android.os.AsyncTask
import io.reactivex.Observable

class FavouriteRecipeRepository(application: Application?) {
    private var fRDao: FavouriteRecipeDao
    val allFR: Observable<List<FavouriteRecipe>>

    init {
        val db: FavouriteRecipeDatabase = FavouriteRecipeDatabase.getInstance(application)
        fRDao = db.favouriteRecipeDao()
        allFR = fRDao.allFR
    }

    fun delete(favouriteRecipe: FavouriteRecipe?) {
        DeleteFRAsyncTasks(fRDao).execute(favouriteRecipe)
    }

    fun insert(favouriteRecipe: FavouriteRecipe?) {
        InsertFRAsyncTasks(fRDao).execute(favouriteRecipe)
    }

    fun deleteAllFR() {
        DeleteAllFRsAsyncTasks(fRDao).execute()
    }

    fun deleteWithKey(primaryKey: Int) {
        DeleteFRWithKeyAsyncTasks(fRDao).execute(primaryKey)
    }

    private inner class DeleteFRWithKeyAsyncTasks(private val fRDao: FavouriteRecipeDao) :
        AsyncTask<Int, Unit, Unit>() {
        override fun doInBackground(vararg p0: Int?) {
            p0[0]?.let { fRDao.deleteFRwithKey(it) }
        }
    }

    private inner class DeleteFRAsyncTasks(private val fRDao: FavouriteRecipeDao) :
        AsyncTask<FavouriteRecipe, Unit, Unit>() {
        override fun doInBackground(vararg favouriteRecipes: FavouriteRecipe) {
            fRDao.deleteFR(favouriteRecipes[0])
        }
    }

    private inner class InsertFRAsyncTasks(private val fRDao: FavouriteRecipeDao) :
        AsyncTask<FavouriteRecipe, Unit, Unit>() {
        protected override fun doInBackground(vararg favouriteRecipes: FavouriteRecipe) {
            fRDao.insertFR(favouriteRecipes[0])
        }
    }

    private inner class DeleteAllFRsAsyncTasks(private val fRDao: FavouriteRecipeDao) :
        AsyncTask<Unit, Unit, Unit>() {
        protected override fun doInBackground(vararg voids: Unit) {
            fRDao.deleteAllFR()
        }
    }
}