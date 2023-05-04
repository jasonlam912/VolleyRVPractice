package com.jasonstudio.cookbook2.FavouriteRecipeModel

import com.jasonstudio.cookbook2.FavouriteRecipeModel.FavouriteRecipe
import com.jasonstudio.cookbook2.FavouriteRecipeModel.FavouriteRecipeDao
import com.jasonstudio.cookbook2.FavouriteRecipeModel.FavouriteRecipeDatabase
import androidx.lifecycle.AndroidViewModel
import android.os.AsyncTask
import androidx.room.*

@Entity(tableName = "Favourite_Recipe_Table")
data class FavouriteRecipe(val id: String, val title: String) {
    @PrimaryKey(autoGenerate = true)
    var primaryKey = 0

}