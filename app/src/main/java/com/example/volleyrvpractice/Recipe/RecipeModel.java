package com.example.volleyrvpractice.Recipe;

public class RecipeModel {
    private String recipeId;
    private String recipeTitle;
    private String recipeImageUrl;
    private Integer statusForDisplay;
    private boolean fRIndicator;

    public RecipeModel(String recipeId, String recipeTitle, String recipeImageUrl, Integer statusForDisplay, boolean fRIndicator) {
        this.recipeId = recipeId;
        this.recipeTitle = recipeTitle;
        this.recipeImageUrl = recipeImageUrl;
        this.statusForDisplay = statusForDisplay;
        this.fRIndicator = fRIndicator;
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
}
