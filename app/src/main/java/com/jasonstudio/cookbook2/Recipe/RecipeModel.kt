package com.example.volleyrvpractice.Recipe

import com.example.volleyrvpractice.Recipe.JsonData2Recipe.jsonObject2RandomRecipe
import com.example.volleyrvpractice.Recipe.JsonData2Recipe.jsonObject2SearchRecipe
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import com.example.volleyrvpractice.Recipe.RecipeModel
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.Glide
import com.example.volleyrvpractice.Network.NetworkManager
import com.example.volleyrvpractice.Network.CallbackListener
import kotlin.Throws
import org.json.JSONException
import org.json.JSONObject
import com.example.volleyrvpractice.Recipe.JsonData2Recipe
import com.example.volleyrvpractice.FavouriteRecipeModel.FavouriteRecipe
import com.example.volleyrvpractice.Network.GlideManager
import com.example.volleyrvpractice.Network.GlideCallbackListener

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