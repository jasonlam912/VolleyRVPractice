package com.jasonstudio.cookbook2.FavouriteRecipeModel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Favourite_Recipe_Table")
data class FavouriteRecipe(val id: String, val title: String) {
    @PrimaryKey(autoGenerate = true)
    var primaryKey = 0

}