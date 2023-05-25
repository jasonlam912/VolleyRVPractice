package com.jasonstudio.cookbook2.FavouriteRecipeModel

import androidx.room.*
import io.reactivex.Observable

@Dao
interface FavouriteRecipeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFR(fr: FavouriteRecipe?)

    @Delete
    suspend fun deleteFR(fr: FavouriteRecipe?)

    @Query("Delete from favourite_recipe_table")
    suspend fun deleteAllFR()

    @get:Query("Select * from favourite_recipe_table")
    val allFR: Observable<List<FavouriteRecipe>>

    @Query("Delete from favourite_recipe_table where primaryKey =:primaryKey")
    suspend fun deleteFRwithKey(primaryKey: Int)
}