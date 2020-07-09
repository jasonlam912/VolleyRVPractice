package com.example.volleyrvpractice.Recipe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonData2Recipe {
    private static String subImageUrl1 = "https://spoonacular.com/recipeImages/";
    private static String subImageUrl2 = "-556x370.jpg";

    public static List<RecipeModel> jsonObject2Recipe(JSONObject jsonObject) throws JSONException {
        List<RecipeModel> list = new ArrayList<>();
        JSONArray resultsArray = jsonObject.getJSONArray("results");

        for(int i =0; i<resultsArray.length();i++){
            JSONObject recipeData = (JSONObject) resultsArray.get(i);
            String recipe_id = recipeData.getString("id");
            String recipe_title = recipeData.getString("title");
            String recipe_imageUrl = subImageUrl1+recipeData.getString("id")+subImageUrl2;
            list.add(new RecipeModel(recipe_id, recipe_title, recipe_imageUrl, 0, false));
        }

        return list;
    }
}
