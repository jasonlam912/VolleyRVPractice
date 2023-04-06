package com.example.cookbook2.FavouriteRecipeModel;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = FavouriteRecipe.class, version = 1)
public abstract class FavouriteRecipeDatabase extends RoomDatabase {
    private static FavouriteRecipeDatabase instance;
    public abstract FavouriteRecipeDao favouriteRecipeDao();
    public synchronized static FavouriteRecipeDatabase getInstance(Context ct){
        if(instance == null){
            instance = Room.databaseBuilder(ct, FavouriteRecipeDatabase.class, "FRList")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }


}
