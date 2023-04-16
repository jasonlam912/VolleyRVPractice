package com.jasonstudio.cookbook2.FavouriteRecipeModel

import com.jasonstudio.cookbook2.FavouriteRecipeModel.FavouriteRecipe
import com.jasonstudio.cookbook2.FavouriteRecipeModel.FavouriteRecipeDao
import com.jasonstudio.cookbook2.FavouriteRecipeModel.FavouriteRecipeDatabase
import androidx.lifecycle.AndroidViewModel
import com.jasonstudio.cookbook2.FavouriteRecipeModel.FavouriteRecipeRepository
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