package com.example.cookbook2.FavouriteRecipe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.cookbook2.FavouriteRecipeModel.FavouriteRecipe;
import com.example.cookbook2.FavouriteRecipeModel.FavouriteRecipeViewModel;
import com.example.cookbook2.R;
import com.example.cookbook2.Recipe.JsonData2Recipe;
import com.example.cookbook2.Recipe.RecipeModel;
import com.example.cookbook2.RecipeAdapter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class FavouriteRecipeActivity extends AppCompatActivity {

    private final static String TAG = FavouriteRecipeActivity.class.getName();

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
        setTitle("Favourite Recipes");

        rv = findViewById(R.id.favourite_recipe_rv);
        adapter = new RecipeAdapter(this, new ArrayList<RecipeModel>());
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(FavouriteRecipeActivity.this, RecyclerView.VERTICAL, false));
        fRViewModel = new ViewModelProvider(this).get(FavouriteRecipeViewModel.class);
        Disposable d =  fRViewModel.getAll().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<FavouriteRecipe>>() {
                    @Override
                    public void accept(List<FavouriteRecipe> favouriteRecipes) throws Exception {
                        List<RecipeModel> recipeList = new ArrayList<>();
                        for(int i=0; i<favouriteRecipes.size();i++){
                            FavouriteRecipe recipe = favouriteRecipes.get(i);
                            String url = JsonData2Recipe.subImageUrl1+recipe.getId()+JsonData2Recipe.subImageUrl2;
                            Log.d(TAG, url);
                            RecipeModel r = new RecipeModel(recipe.getId(),recipe.getTitle(), url, 0, true, null);
                            recipeList.add(r);
                        }
                        adapter.modifyData(recipeList);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
            /*
                observe(this, new Observer<List<FavouriteRecipe>>() {
            @Override
            public void onChanged(List<FavouriteRecipe> favouriteRecipes) {


            }
        });*/

    }



}
