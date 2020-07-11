package com.example.volleyrvpractice.Recipe;

import com.example.volleyrvpractice.FavouriteRecipeModel.FavouriteRecipe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonData2Recipe {
    public static String subImageUrl1 = "https://spoonacular.com/recipeImages/";
    public static String subImageUrl2 = "-556x370.jpg";

    public static List<RecipeModel> jsonObject2RandomRecipe(JSONObject jsonObject, List<FavouriteRecipe> fRList) throws JSONException {
        List<RecipeModel> list = new ArrayList<>();
        JSONArray resultsArray = jsonObject.getJSONArray("recipes");

        for(int i =0; i<resultsArray.length();i++){
            JSONObject recipeData = (JSONObject) resultsArray.get(i);
            String recipe_id = recipeData.getString("id");
            String recipe_title = recipeData.getString("title");
            String recipe_imageUrl = subImageUrl1+recipeData.getString("id")+subImageUrl2;

            boolean fRIndicator = false;
            for(FavouriteRecipe fR: fRList){
                if(fR.getId().equals(recipe_id)){
                    fRIndicator = true;
                }
            }

            list.add(new RecipeModel(recipe_id, recipe_title, recipe_imageUrl, 0, fRIndicator, null));
        }

        return list;
    }

    public static List<RecipeModel> jsonObject2SearchRecipe(JSONObject jsonObject, List<FavouriteRecipe> fRList) throws JSONException {
        List<RecipeModel> list = new ArrayList<>();
        JSONArray resultsArray = jsonObject.getJSONArray("results");

        for(int i =0; i<resultsArray.length();i++){
            JSONObject recipeData = (JSONObject) resultsArray.get(i);
            String recipe_id = recipeData.getString("id");
            String recipe_title = recipeData.getString("title");
            String recipe_imageUrl = subImageUrl1+recipeData.getString("id")+subImageUrl2;

            boolean fRIndicator = false;
            for(FavouriteRecipe fR: fRList){
                if(fR.getId().equals(recipe_id)){
                    fRIndicator = true;
                }
            }

            list.add(new RecipeModel(recipe_id, recipe_title, recipe_imageUrl, 0, fRIndicator, null));
        }

        return list;
    }
}
