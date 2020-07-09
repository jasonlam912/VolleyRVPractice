package com.example.volleyrvpractice.FavouriteRecipe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.volleyrvpractice.FavouriteRecipeModel.FavouriteRecipe;
import com.example.volleyrvpractice.FavouriteRecipeModel.FavouriteRecipeViewModel;
import com.example.volleyrvpractice.R;
import com.example.volleyrvpractice.RecipeAdapter;

import java.util.ArrayList;
import java.util.List;

public class FavouriteRecipeActivity extends AppCompatActivity {

    private FavouriteRecipeViewModel fRViewModel;
    private RecyclerView rv;
    private List<String> fRTitles = new ArrayList<>();
    private List<String> fRIDs = new ArrayList<>();
    private List<String> fRImageUrls = new ArrayList<>();
    private List<Integer> fRStatus = new ArrayList<>();
    private List<Boolean> fRIndicators = new ArrayList<>();
    private String subImageUrl1 = "https://spoonacular.com/recipeImages/";
    private String subImageUrl2 = "-556x370.jpg";
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
        adapter = new RecipeAdapter(FavouriteRecipeActivity.this, fRTitles, fRImageUrls, fRIDs, fRStatus, fRIndicators);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(FavouriteRecipeActivity.this, RecyclerView.VERTICAL, false));
        fRViewModel = new ViewModelProvider(this).get(FavouriteRecipeViewModel.class);
        fRViewModel.getAll().observe(this, new Observer<List<FavouriteRecipe>>() {
            @Override
            public void onChanged(List<FavouriteRecipe> favouriteRecipes) {
                /*for(int i=0; i<favouriteRecipes.size();i++){
                    Log.d(Integer.toString(i)+" item", favouriteRecipes.get(i).getId());
                }*/
                inputData(favouriteRecipes);
                adapter.modifyData(fRTitles, fRImageUrls, fRIDs, fRStatus, fRIndicators);
            }
        });

    }



    private void inputData(List<FavouriteRecipe> fRs){
        fRTitles = new ArrayList<>();
        fRIDs = new ArrayList<>();
        fRImageUrls = new ArrayList<>();
        fRStatus = new ArrayList<>();
        fRIndicators = new ArrayList<>();

        for(int i=0; i<fRs.size();i++){
            FavouriteRecipe fR = fRs.get(i);
            fRTitles.add(fR.getTitle());
            fRIDs.add(fR.getId());
            fRImageUrls.add(subImageUrl1+fR.getId()+subImageUrl2);
            fRStatus.add(0);
            fRIndicators.add(true);
        }
        fRTitles.add(null);
        fRIDs.add(null);
        fRImageUrls.add(null);
        fRStatus.add(2);
    }
}
