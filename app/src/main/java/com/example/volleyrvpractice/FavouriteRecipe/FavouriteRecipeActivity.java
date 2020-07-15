package com.example.volleyrvpractice.FavouriteRecipe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.volleyrvpractice.FavouriteRecipeModel.FavouriteRecipe;
import com.example.volleyrvpractice.FavouriteRecipeModel.FavouriteRecipeViewModel;
import com.example.volleyrvpractice.R;
import com.example.volleyrvpractice.Recipe.JsonData2Recipe;
import com.example.volleyrvpractice.Recipe.RecipeModel;
import com.example.volleyrvpractice.RecipeAdapter;

import java.util.ArrayList;
import java.util.List;

public class FavouriteRecipeActivity extends AppCompatActivity {

    private FavouriteRecipeViewModel fRViewModel;
    private RecyclerView rv;
    private RecipeAdapter adapter;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.favourite_recipe_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.delete_all_recipe){
            fRViewModel.deleteAll();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_recipe);
        rv = findViewById(R.id.favourite_recipe_rv);
        adapter = new RecipeAdapter(this, new ArrayList<RecipeModel>());
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(FavouriteRecipeActivity.this, RecyclerView.VERTICAL, false));
        fRViewModel = new ViewModelProvider(this).get(FavouriteRecipeViewModel.class);
        fRViewModel.getAll().observe(this, new Observer<List<FavouriteRecipe>>() {
            @Override
            public void onChanged(List<FavouriteRecipe> favouriteRecipes) {
                List<RecipeModel> recipeList = new ArrayList<>();
                for(int i=0; i<favouriteRecipes.size();i++){
                    FavouriteRecipe recipe = favouriteRecipes.get(i);
                    String url = JsonData2Recipe.subImageUrl1+recipe.getId()+JsonData2Recipe.subImageUrl2;
                    RecipeModel r = new RecipeModel(recipe.getId(),recipe.getTitle(), url, 0, true, null);
                    recipeList.add(r);
                }
                adapter.modifyData(recipeList);
            }
        });

    }



}
