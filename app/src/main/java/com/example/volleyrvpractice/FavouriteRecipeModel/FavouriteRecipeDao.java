package com.example.volleyrvpractice.FavouriteRecipeModel;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FavouriteRecipeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFR(FavouriteRecipe fr);
    @Delete
    void deleteFR(FavouriteRecipe fr);
    @Query("Delete from favourite_recipe_table")
    void deleteAllFR();
    @Query("Select * from favourite_recipe_table")
    LiveData<List<FavouriteRecipe>> getAllFR();
}
