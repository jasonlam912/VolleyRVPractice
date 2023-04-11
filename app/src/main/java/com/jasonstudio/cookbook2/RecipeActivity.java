package com.jasonstudio.cookbook2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jasonstudio.cookbook2.FavouriteRecipeModel.FavouriteRecipe;
import com.jasonstudio.cookbook2.FavouriteRecipeModel.FavouriteRecipeViewModel;
import com.jasonstudio.cookbook2.IngredientClasses.IngredientFragement;
import com.jasonstudio.cookbook2.RecipeInstructionClasses.RecipeInstructionFragment;
import com.jasonstudio.cookbook2.RecipeNutritionClasses.NutritionFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class RecipeActivity extends AppCompatActivity {

    //Toolbar UI objecct ---------------------------------------------------------------------------
    private String subImageUrl1 = "https://spoonacular.com/recipeImages/";
    private String subImageUrl2 = "-556x370.jpg";
    private AppCompatTextView recipeTitleTextView;
    private ImageView recipeImageView;
    //Toolbar UI objecct ---------------------------------------------------------------------------

    //Tab UI object---------------------------------------------------------------------------------
    private ViewPager viewPager;
    private TabLayout tabLayout;

    private RecipeInstructionFragment recipeInstructionFragment;
    private IngredientFragement ingredientFragement;
    private NutritionFragment nutritionFragment;
    //Tab UI object---------------------------------------------------------------------------------

    //Data Object-----------------------------------------------------------------------------------
    private String recipe_id;
    private String recipe_title;
    private Bitmap recipe_image;
    private boolean is_favourite_recipe;
    private FavouriteRecipeViewModel fRViewModel;
    //Data Object-----------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("onCreateRcipeActivity", "here");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);


        recipe_id = getIntent().getStringExtra("id");
        recipe_title = getIntent().getStringExtra("title");
        recipe_image = getIntent().getParcelableExtra("recipe_image");
        initializeToolBar();
        is_favourite_recipe = getIntent().getBooleanExtra("favourite_recipe",true);
        //Log.d("onCreate",recipe_id);
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);


        Bundle bundleIgd = bundleForIngredientFragment();

        recipeInstructionFragment = new RecipeInstructionFragment();
        recipeInstructionFragment.setArguments(bundleIgd);
        ingredientFragement = new IngredientFragement();
        ingredientFragement.setArguments(bundleIgd);
        nutritionFragment = new NutritionFragment();
        nutritionFragment.setArguments(bundleIgd);

        tabLayout.setupWithViewPager(viewPager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),0);

        viewPagerAdapter.addFragment(ingredientFragement, "Ingredients");
        viewPagerAdapter.addFragment(recipeInstructionFragment, "Steps");
        viewPagerAdapter.addFragment(nutritionFragment, "Nutritions");

        viewPager.setAdapter(viewPagerAdapter);



        tabLayout.getTabAt(0).setIcon(R.drawable.ic_ingredient);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_recipe_instruction);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_nutrition);

        /*
        for(int i=0; i<3; i++){
            tabLayout.getTabAt(i).getOrCreateBadge().setNumber(1);
        }*/
        fRViewModel = new ViewModelProvider(this).get(FavouriteRecipeViewModel.class);

    }

    private void initializeToolBar(){
        recipeTitleTextView = findViewById(R.id.recipe_title);
        recipeImageView = findViewById(R.id.recipe_image);
        recipeTitleTextView.setText(recipe_title);
        //recipeImageView.setImageBitmap(recipe_image);
        Glide.with(this).load(subImageUrl1+recipe_id+subImageUrl2).diskCacheStrategy(DiskCacheStrategy.ALL).into(recipeImageView);
        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(recipe_title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recipe_information_menu, menu);

        ToggleButton button = (ToggleButton) menu.findItem(R.id.menu_favourite_recipe).getActionView().findViewById(R.id.menu_favourite_recipe_toggle_button);
        button.setChecked(is_favourite_recipe);
        button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    FavouriteRecipe recipe = new FavouriteRecipe(recipe_id, recipe_title);
                    recipe.setPrimaryKey(Integer.valueOf(recipe_id));
                    fRViewModel.insert(recipe);
                    Snackbar.make(getWindow().getDecorView(),"\'"+recipe_title+"\'"+" is added to the favourite recipe list.", Snackbar.LENGTH_LONG).show();
                }else{
                    FavouriteRecipe recipe = new FavouriteRecipe(recipe_id, recipe_title);
                    recipe.setPrimaryKey(Integer.valueOf(recipe_id));
                    fRViewModel.delete(recipe);
                    Snackbar.make(getWindow().getDecorView(),"\'"+recipe_title+"\'"+" is deleted to the favourite recipe list.", Snackbar.LENGTH_LONG).show();
                }
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private List<String> fragmentTitles = new ArrayList<>();
        private List<Fragment> fragments = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        public void addFragment(Fragment fragment, String title){
            fragmentTitles.add(title);
            fragments.add(fragment);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitles.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

    private Bundle bundleForIngredientFragment(){
        Bundle bundle = new Bundle();
        bundle.putString("recipe_id",recipe_id);
        /*recipeData = new HashMap<>();
        recipeData.put("ingredient_image_url", new ArrayList<String>());
        recipeData.put("ingredient_title", new ArrayList<String>());
        recipeData.put("ingredient_amount", new ArrayList<String>());
        requestQueue = Volley.newRequestQueue(this);
        loadRecipeDetails();
        bundle.putSerializable("recipeDataHashMap", recipeData);*/
        return bundle;
    }

    static private String convertDecimalToFraction(double x){
        if (x < 0){
            return "-" + convertDecimalToFraction(-x);
        }
        double tolerance = 1.0E-6;
        double h1=1; double h2=0;
        double k1=0; double k2=1;
        double b = x;
        do {
            double a = Math.floor(b);
            double aux = h1; h1 = a*h1+h2; h2 = aux;
            aux = k1; k1 = a*k1+k2; k2 = aux;
            b = 1/(b-a);
        } while (Math.abs(x-h1/k1) > x*tolerance);

        int h1result = (int) h1;
        int k1result = (int) k1;
        String result=((k1result==1)?Integer.toString(h1result):(h1result+"/"+k1result));
        return result;
    }


}
