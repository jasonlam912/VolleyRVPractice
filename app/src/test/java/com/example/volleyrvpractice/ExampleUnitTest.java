package com.example.volleyrvpractice;

import com.android.volley.toolbox.Volley;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void a(){
        RecipeActivity recipeActivity = new RecipeActivity();
        recipeActivity.loadRecipeDetails();
    }
}