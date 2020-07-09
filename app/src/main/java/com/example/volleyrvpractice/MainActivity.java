package com.example.volleyrvpractice;

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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.volleyrvpractice.FavouriteRecipe.FavouriteRecipeActivity;
import com.example.volleyrvpractice.FavouriteRecipeModel.FavouriteRecipe;
import com.example.volleyrvpractice.FavouriteRecipeModel.FavouriteRecipeViewModel;
import com.example.volleyrvpractice.Network.NetworkManager;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView rv;
    RecipeAdapter adapter;
    LinearLayoutManager manager;
    List<String> recipe_title = new ArrayList<>();
    List<String> image_link = new ArrayList<>();
    List<String> recipe_id = new ArrayList<>();
    List<Integer> recipe_status = new ArrayList<>();
    List<Boolean> fRIndicators = new ArrayList<>();
    private FavouriteRecipeViewModel fRViewModel;
    private List<FavouriteRecipe> tempFRs;

    private DrawerLayout drawerLayout;
    private NavigationView nv;
    private Toolbar toolbar;
    static boolean loadmore=true;
    static boolean searching=false;
    static String searchString;



    private String subSearchUrl1 = "https://api.spoonacular.com/recipes/complexSearch?number=10&apiKey=";
    private String subSearchUrl1_1 = "&query=";
    private String subSearchUrl2 = "&offset=";
    private String subImageUrl1 = "https://spoonacular.com/recipeImages/";
    private String subImageUrl2 = "-556x370.jpg";
    //volley things
    private ProgressBar loadingRecipePB;
    private SwipeRefreshLayout swipeRecipeRVContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeDrawerLayout();
        loadingRecipePB = findViewById(R.id.loading_recipe_progress_bar);
        loadingRecipePB.setVisibility(View.INVISIBLE);
        swipeRecipeRVContainer = findViewById(R.id.swipe_recipe_rv_container);

        rv = findViewById(R.id.recipe_rv);
        adapter = new RecipeAdapter(this, recipe_title, image_link, recipe_id,recipe_status,fRIndicators);
        rv.setAdapter(adapter);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            manager = new LinearLayoutManager(this);
        }else{
            manager = new GridLayoutManager(this,2);
        }
        rv.setLayoutManager(manager);
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                   @Override
                                   public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                                       super.onScrolled(recyclerView, dx, dy);

                                       int lastVisiblePosition = manager.findLastVisibleItemPosition();

                                       if (loadmore&&lastVisiblePosition == recipe_id.size()-1) {

                                           loadmore = false;
                                           if(searching){
                                               loadSearchList(recipe_id.size(),searchString);
                                           }else{
                                               loadRandomList(recipe_id.size());
                                           }
                                       }
                                   }
                               }
        );
        fRViewModel = new ViewModelProvider(this).get(FavouriteRecipeViewModel.class);
        Observer<List<FavouriteRecipe>> observer = new Observer<List<FavouriteRecipe>>() {
            @Override
            public void onChanged(List<FavouriteRecipe> favouriteRecipes) {
                tempFRs = favouriteRecipes;
                loadRandomList(0);
                fRViewModel.getAll().removeObserver(this);
            }
        };
        fRViewModel.getAll().observe(this, observer);
        swipeRecipeRVContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadRandomList(0);
            }
        });
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
                searching = true;
                Log.d("onQueryTextSubmit", query);
                loadSearchList(0,query);
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
        toolbar.setNavigationIcon(R.drawable.recipes);
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
            searching = false;
            loadingRecipePB.setVisibility(View.VISIBLE);
            loadRandomList(0);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadSearchList(final int offset, String query){
        if(offset==0){
            loadingRecipePB.setVisibility(View.VISIBLE);
        }
        String url = subSearchUrl1+ getResources().getString(R.string.apiKeyUsing)+subSearchUrl1_1+URLEncoder.encode(query)+subSearchUrl2+offset;
        Log.d("loadSearchList", url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            addSearchList(offset, response);
                            Log.d("onResponse",response.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(50000,5,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        NetworkManager.getInstance(this.getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void addSearchList(int offset, JSONObject response) throws JSONException {
        JSONArray resultsArray = response.getJSONArray("results");
        if(offset==0){
            initializeData();
            addPaddingItem();
        }else{
            deleteLastItem();
        }

        for(int i =0; i<resultsArray.length();i++){
            JSONObject recipeData = (JSONObject) resultsArray.get(i);
            recipe_id.add(recipeData.getString("id"));
            recipe_title.add(recipeData.getString("title"));
            image_link.add(subImageUrl1+recipeData.getString("id")+subImageUrl2);
            recipe_status.add(0);
            fRIndicators.add(checkIdInsideFRList(recipeData.getString("id")));
        }

        if(resultsArray.length()==0){
            addEndItem();
            loadmore=false;
        }else{
            addLoadItem();
            loadmore=true;
        }
        adapter.modifyData(recipe_title,image_link,recipe_id,recipe_status, fRIndicators);
        loadingRecipePB.setVisibility(View.INVISIBLE);
        Log.d("fRIndicators",fRIndicators.toString());
    }

    private void initializeData(){
        recipe_title = new ArrayList<>();
        recipe_id = new ArrayList<>();
        image_link = new ArrayList<>();
        recipe_status = new ArrayList<>();
        fRIndicators = new ArrayList<>();
    }

    private void addPaddingItem(){
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            recipe_status.add(3); recipe_title.add(null); recipe_id.add(null); image_link.add(null); fRIndicators.add(null);
            recipe_status.add(3); recipe_title.add(null); recipe_id.add(null); image_link.add(null); fRIndicators.add(null);
        }else{
            recipe_status.add(3); recipe_title.add(null); recipe_id.add(null); image_link.add(null); fRIndicators.add(null);
        }

    }

    private void addEndItem(){
        recipe_status.add(2); recipe_title.add(null); recipe_id.add(null); image_link.add(null); fRIndicators.add(null);
    }

    private void addLoadItem(){
        recipe_status.add(1); recipe_title.add(null); recipe_id.add(null); image_link.add(null); fRIndicators.add(null);
    }

    private void deleteLastItem(){
        recipe_status.remove(recipe_status.size()-1); recipe_title.remove(recipe_title.size()-1);
        recipe_id.remove(recipe_id.size()-1); image_link.remove(image_link.size()-1);
        fRIndicators.remove(fRIndicators.size()-1);
    }

    private void loadRandomList(final Integer lastIndex){

        String url = "https://api.spoonacular.com/recipes/random?apiKey="+getResources().getString(R.string.apiKeyUsing)+"&number=10";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            addRandomList(lastIndex,response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        error.printStackTrace();
                    }
                });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(50000,5,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        NetworkManager.getInstance(this.getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void addRandomList(int offset, JSONObject response) throws JSONException {
        JSONArray recipesArray = response.getJSONArray("recipes");
        if(offset==0){
            initializeData();
            addPaddingItem();
        }else{
            deleteLastItem();
        }

        for(int i =0; i<recipesArray.length();i++){
            JSONObject recipeData = (JSONObject) recipesArray.get(i);
            recipe_id.add(recipeData.getString("id"));
            recipe_title.add(recipeData.getString("title"));
            image_link.add(subImageUrl1+recipeData.getString("id")+subImageUrl2);
            recipe_status.add(0);
            fRIndicators.add(checkIdInsideFRList(recipeData.getString("id")));
        }
        Log.d("fRIndicators",fRIndicators.toString());

        if(recipesArray.length()==0){
            addEndItem();
            loadmore=false;
        }else{
            addLoadItem();
            loadmore=true;
        }
        adapter.modifyData(recipe_title,image_link,recipe_id,recipe_status, fRIndicators);
        loadingRecipePB.setVisibility(View.INVISIBLE);
        swipeRecipeRVContainer.setRefreshing(false);
    }

    private boolean checkIdInsideFRList(String recipe_id){
        for(int j=0; j<tempFRs.size(); j++){
            if(recipe_id.equals(tempFRs.get(j).getId())) {
                return true;
            }
        }
        return false;
    }

}
