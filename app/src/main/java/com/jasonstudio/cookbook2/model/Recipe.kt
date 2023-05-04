package com.jasonstudio.cookbook2.model

import com.jasonstudio.cookbook2.Recipe.JsonData2Recipe
import com.jasonstudio.cookbook2.Recipe.RecipeModel

data class Recipe(
    val id: String,
    val title: String,
) {
    fun toRecipeModel(): RecipeModel {
        return RecipeModel(
            id,
            title,
            JsonData2Recipe.subImageUrl1 + id + JsonData2Recipe.subImageUrl2,
            0, false, null
        )
    }
}
