package com.example.volleyrvpractice.FavouriteRecipeModel

import com.example.volleyrvpractice.FavouriteRecipeModel.FavouriteRecipe
import com.example.volleyrvpractice.FavouriteRecipeModel.FavouriteRecipeDao
import com.example.volleyrvpractice.FavouriteRecipeModel.FavouriteRecipeDatabase
import androidx.lifecycle.AndroidViewModel
import com.example.volleyrvpractice.FavouriteRecipeModel.FavouriteRecipeRepository
import android.os.AsyncTask
import androidx.room.*
import io.reactivex.Observable

@Dao
interface FavouriteRecipeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFR(fr: FavouriteRecipe?)

    @Delete
    fun deleteFR(fr: FavouriteRecipe?)

    @Query("Delete from favourite_recipe_table")
    fun deleteAllFR()

    @get:Query("Select * from favourite_recipe_table")
    val allFR: Observable<List<FavouriteRecipe>>

    @Query("Delete from favourite_recipe_table where primaryKey =:primaryKey")
    fun deleteFRwithKey(primaryKey: Int)
}