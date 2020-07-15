package com.example.volleyrvpractice.Recipe;

import android.graphics.Bitmap;

public class RecipeModel {
    private String recipeId;
    private String recipeTitle;
    private String recipeImageUrl;
    private Integer statusForDisplay;
    private boolean fRIndicator;
    private Bitmap recipeIamge;

    public RecipeModel(String recipeId, String recipeTitle, String recipeImageUrl, Integer statusForDisplay, boolean fRIndicator, Bitmap recipeImage) {
        this.recipeId = recipeId;
        this.recipeTitle = recipeTitle;
        this.recipeImageUrl = recipeImageUrl;
        this.statusForDisplay = statusForDisplay;
        this.fRIndicator = fRIndicator;
        this.recipeIamge = recipeImage;
    }

    public RecipeModel(String recipeId, String recipeTitle, String recipeImageUrl, Integer statusForDisplay) {
        this.recipeId = recipeId;
        this.recipeTitle = recipeTitle;
        this.recipeImageUrl = recipeImageUrl;
        this.statusForDisplay = statusForDisplay;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public String getRecipeTitle() {
        return recipeTitle;
    }

    public String getRecipeImageUrl() {
        return recipeImageUrl;
    }

    public Integer getStatusForDisplay() {
        return statusForDisplay;
    }

    public boolean isfRIndicator() {
        return fRIndicator;
    }

    public Bitmap getRecipeIamge() {
        return recipeIamge;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public void setRecipeTitle(String recipeTitle) {
        this.recipeTitle = recipeTitle;
    }

    public void setRecipeImageUrl(String recipeImageUrl) {
        this.recipeImageUrl = recipeImageUrl;
    }

    public void setStatusForDisplay(Integer statusForDisplay) {
        this.statusForDisplay = statusForDisplay;
    }

    public void setfRIndicator(boolean fRIndicator) {
        this.fRIndicator = fRIndicator;
    }

    public void setRecipeImage(Bitmap recipeIamge) {
        this.recipeIamge = recipeIamge;
    }
}
