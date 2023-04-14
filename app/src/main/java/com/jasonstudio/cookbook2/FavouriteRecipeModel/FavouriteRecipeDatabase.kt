package com.example.volleyrvpractice.FavouriteRecipeModel

import android.content.Context
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

@Database(entities = [FavouriteRecipe::class], version = 1)
abstract class FavouriteRecipeDatabase : RoomDatabase() {
    abstract fun favouriteRecipeDao(): FavouriteRecipeDao

    companion object {
        private var instance: FavouriteRecipeDatabase? = null
        @Synchronized
        fun getInstance(ct: Context?): FavouriteRecipeDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    ct!!, FavouriteRecipeDatabase::class.java, "FRList"
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return instance!!
        }
    }
}