package com.example.volleyrvpractice.FavouriteRecipeModel

import com.example.volleyrvpractice.FavouriteRecipeModel.FavouriteRecipe
import com.example.volleyrvpractice.FavouriteRecipeModel.FavouriteRecipeDao
import com.example.volleyrvpractice.FavouriteRecipeModel.FavouriteRecipeDatabase
import androidx.lifecycle.AndroidViewModel
import android.os.AsyncTask
import androidx.room.*

@Entity(tableName = "Favourite_Recipe_Table")
class FavouriteRecipe(val id: String, val title: String) {
    @PrimaryKey(autoGenerate = true)
    var primaryKey = 0

}