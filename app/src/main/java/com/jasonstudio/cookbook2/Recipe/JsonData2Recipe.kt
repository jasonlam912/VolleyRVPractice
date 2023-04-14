package com.example.volleyrvpractice.Recipe

import kotlin.Throws
import org.json.JSONException
import org.json.JSONObject
import com.example.volleyrvpractice.FavouriteRecipeModel.FavouriteRecipe
import com.example.volleyrvpractice.Recipe.RecipeModel
import org.json.JSONArray
import com.example.volleyrvpractice.Recipe.JsonData2Recipe
import java.util.ArrayList

object JsonData2Recipe {
    @JvmField
    var subImageUrl1 = "https://spoonacular.com/recipeImages/"
    @JvmField
    var subImageUrl2 = "-556x370.jpg"
    @JvmStatic
    @Throws(JSONException::class)
    fun jsonObject2RandomRecipe(
        jsonObject: JSONObject,
        fRList: List<FavouriteRecipe>
    ): List<RecipeModel> {
        val list: MutableList<RecipeModel> = ArrayList()
        val resultsArray = jsonObject.getJSONArray("recipes")
        for (i in 0 until resultsArray.length()) {
            val recipeData = resultsArray[i] as JSONObject
            val recipe_id = recipeData.getString("id")
            val recipe_title = recipeData.getString("title")
            val recipe_imageUrl = subImageUrl1 + recipeData.getString("id") + subImageUrl2
            var fRIndicator = false
            for (fR in fRList) {
                if (fR.id == recipe_id) {
                    fRIndicator = true
                }
            }
            list.add(RecipeModel(recipe_id, recipe_title, recipe_imageUrl, 0, fRIndicator, null))
        }
        return list
    }

    @JvmStatic
    @Throws(JSONException::class)
    fun jsonObject2SearchRecipe(
        jsonObject: JSONObject,
        fRList: List<FavouriteRecipe>
    ): List<RecipeModel> {
        val list: MutableList<RecipeModel> = ArrayList()
        val resultsArray = jsonObject.getJSONArray("results")
        for (i in 0 until resultsArray.length()) {
            val recipeData = resultsArray[i] as JSONObject
            val recipe_id = recipeData.getString("id")
            val recipe_title = recipeData.getString("title")
            val recipe_imageUrl = subImageUrl1 + recipeData.getString("id") + subImageUrl2
            var fRIndicator = false
            for (fR in fRList) {
                if (fR.id == recipe_id) {
                    fRIndicator = true
                }
            }
            list.add(RecipeModel(recipe_id, recipe_title, recipe_imageUrl, 0, fRIndicator, null))
        }
        return list
    }
}