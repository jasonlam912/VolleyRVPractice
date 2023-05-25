package com.jasonstudio.cookbook2.FavouriteRecipeModel

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jasonstudio.cookbook2.FavouriteRecipeModel.FavouriteRecipeDatabase

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