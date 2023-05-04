package com.jasonstudio.cookbook2.model

import com.squareup.moshi.Json

data class IngredientsResponse(
    @Json(name = "extendedIngredients")
    val extendedIngredients: MutableList<Ingredient>

)
