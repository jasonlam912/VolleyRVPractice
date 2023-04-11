package com.jasonstudio.cookbook2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.jasonstudio.cookbook2.FavouriteRecipe.FavouriteRecipeActivity;
import com.jasonstudio.cookbook2.Recipe.RecipeModel;
import com.jasonstudio.cookbook2.Recipe.RecipeViewModel;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView rv;
    RecipeAdapter adapter;
    LinearLayoutManager manager;

    private RecipeViewModel recipeViewModel;

    private DrawerLayout drawerLayout;
    private NavigationView nv;
    private Toolbar toolbar;

    static String searchString;
    private static int orientation;
    //volley things
    private ProgressBar loadingRecipePB;
    private SwipeRefreshLayout swipeRecipeRVContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeMain);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeDrawerLayout();
        orientation = getResources().getConfiguration().orientation;
        loadingRecipePB = findViewById(R.id.loading_recipe_progress_bar);
        loadingRecipePB.setVisibility(View.INVISIBLE);

        swipeRecipeRVContainer = findViewById(R.id.swipe_recipe_rv_container);

        rv = findViewById(R.id.recipe_rv);
        adapter = new RecipeAdapter(this, new ArrayList<RecipeModel>());
        rv.setAdapter(adapter);
        manager = getManager(orientation);
        rv.setLayoutManager(manager);
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                   @Override
                                   public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                                       super.onScrolled(recyclerView, dx, dy);

                                       int lastVisiblePosition = manager.findLastVisibleItemPosition();
                                       //Log.d("lastPos", Boolean.toString(lastVisiblePosition == recipeViewModel.getData().getValue().size()-1));
                                       if (!recipeViewModel.isLoading.getValue()&&lastVisiblePosition == recipeViewModel.getData().getValue().size()-1) {
                                           //Log.d("isSearch", Boolean.toString(recipeViewModel.isSearch));
                                           recipeViewModel.putRecipe(MainActivity.this, searchString);
                                       }
                                   }
                               }
        );

        recipeViewModel = new ViewModelProvider(this).get(RecipeViewModel.class);
        recipeViewModel.getData().observe(this, new Observer<List<RecipeModel>>() {
            @Override
            public void onChanged(List<RecipeModel> recipeModels) {
                adapter.modifyData(recipeModels);
            }
        });

        swipeRecipeRVContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recipeViewModel.resetData(MainActivity.this);
                recipeViewModel.putRandomRecipe(MainActivity.this);
            }
        });

        recipeViewModel.isLoading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                swipeRecipeRVContainer.setRefreshing(aBoolean);
                /*if(aBoolean){
                    loadingRecipePB.setVisibility(View.VISIBLE);
                }else{
                    loadingRecipePB.setVisibility(View.INVISIBLE);
                }*/
            }
        });

        if(recipeViewModel.getData().getValue().size()==2){
            recipeViewModel.putRandomRecipe(MainActivity.this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.refresh_menu,menu);
        SearchView searchView = (SearchView) MainActivity.this.findViewById(R.id.recipe_search_view);

        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchString=query;
                Log.d("onQueryTextSubmit", query);
                recipeViewModel.putSearchRecipe(MainActivity.this, query);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void initializeDrawerLayout(){
        drawerLayout = findViewById(R.id.drawer_layout);
        nv = findViewById(R.id.navigation_view_drawer);
        toolbar = findViewById(R.id.toolbar2);
        //toolbar.setNavigationIcon(R.drawable.recipes);
        toolbar.setTitle("");

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer,R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.closeDrawer(GravityCompat.START);
                switch (item.getItemId()){
                    case R.id.menu_find_recipe:
                        //item.setChecked(true);
                        return true;
                    case R.id.menu_favourite_recipe:
                        //item.setChecked(true);
                        startActivity(new Intent(MainActivity.this,FavouriteRecipeActivity.class));
                        return true;
                    case R.id.menu_about_us:
                        //item.setChecked(true);
                        return true;
                }
                return false;
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.refresh_recipe_button){
            recipeViewModel.resetData(this);
            recipeViewModel.putRandomRecipe(MainActivity.this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private LinearLayoutManager getManager(int orientation){
        if(orientation == Configuration.ORIENTATION_PORTRAIT){
            return new LinearLayoutManager(this);
        }else{
            GridLayoutManager manager = new GridLayoutManager(this,2);
            manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return adapter.getItemViewType(position)==0?1:2;
                }
            });
            return manager;
        }
    }
}
