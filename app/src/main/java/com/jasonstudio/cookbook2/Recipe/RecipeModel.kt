package com.jasonstudio.cookbook2.Recipe

import android.graphics.Bitmap

data class RecipeModel (
    var recipeId: String?,
    var recipeTitle: String?,
    var recipeImageUrl: String?,
    var statusForDisplay: Int?,
    private var fRIndicator: Boolean?,
    private var _recipeIamge: Bitmap?
) {

    val recipeIamge: Bitmap?
    get() = _recipeIamge

    constructor(
        recipeId: String,
        recipeTitle: String,
        recipeImageUrl: String,
        statusForDisplay: Int
    ) : this(
        recipeId,
        recipeTitle,
        recipeImageUrl,
        statusForDisplay,
        false,
        null
    )

    fun isfRIndicator(): Boolean {
        return fRIndicator ?: false
    }

    fun setfRIndicator(fRIndicator: Boolean) {
        this.fRIndicator = fRIndicator
    }

    fun setRecipeImage(recipeIamge: Bitmap?) {
        this._recipeIamge = recipeIamge
    }
}