package com.example.cookbook2.FavouriteRecipeModel;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Favourite_Recipe_Table")
public class FavouriteRecipe {
    @PrimaryKey(autoGenerate = true)
    private int primaryKey;
    private String id;
    private String title;

    public FavouriteRecipe(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public int getPrimaryKey() {
        return primaryKey;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setPrimaryKey(int primaryKey) {
        this.primaryKey = primaryKey;
    }
}
