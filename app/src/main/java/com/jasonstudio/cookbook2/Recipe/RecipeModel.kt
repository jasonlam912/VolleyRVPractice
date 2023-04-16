package com.jasonstudio.cookbook2.Recipe

import com.jasonstudio.cookbook2.Recipe.JsonData2Recipe.jsonObject2RandomRecipe
import com.jasonstudio.cookbook2.Recipe.JsonData2Recipe.jsonObject2SearchRecipe
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import com.jasonstudio.cookbook2.Recipe.RecipeModel
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.Glide
import com.jasonstudio.cookbook2.Network.NetworkManager
import com.jasonstudio.cookbook2.Network.CallbackListener
import kotlin.Throws
import org.json.JSONException
import org.json.JSONObject
import com.jasonstudio.cookbook2.Recipe.JsonData2Recipe
import com.jasonstudio.cookbook2.FavouriteRecipeModel.FavouriteRecipe
import com.jasonstudio.cookbook2.Network.GlideManager
import com.jasonstudio.cookbook2.Network.GlideCallbackListener

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