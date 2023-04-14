package com.example.volleyrvpractice.FavouriteRecipeModel

import android.app.Application
import androidx.room.PrimaryKey
import androidx.room.Dao
import androidx.room.OnConflictStrategy
import com.example.volleyrvpractice.FavouriteRecipeModel.FavouriteRecipe
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.volleyrvpractice.FavouriteRecipeModel.FavouriteRecipeDao
import com.example.volleyrvpractice.FavouriteRecipeModel.FavouriteRecipeDatabase
import androidx.room.Room
import androidx.lifecycle.AndroidViewModel
import com.example.volleyrvpractice.FavouriteRecipeModel.FavouriteRecipeRepository
import android.os.AsyncTask
import io.reactivex.Observable

class FavouriteRecipeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: FavouriteRecipeRepository
    val all: Observable<List<FavouriteRecipe>>

    init {
        repository = FavouriteRecipeRepository(application)
        all = repository.allFR
    }

    fun insert(recipe: FavouriteRecipe?) {
        repository.insert(recipe)
    }

    fun delete(recipe: FavouriteRecipe?) {
        repository.delete(recipe)
    }

    fun deleteAll() {
        repository.deleteAllFR()
    }

    fun deleteWithKey(key: Int) {
        repository.deleteWithKey(key)
    }
}