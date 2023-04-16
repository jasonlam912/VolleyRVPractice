package com.jasonstudio.cookbook2

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.jasonstudio.cookbook2.RecipeInstructionClasses.RecipeInstructionFragment
import com.jasonstudio.cookbook2.IngredientClasses.IngredientFragement
import com.jasonstudio.cookbook2.RecipeNutritionClasses.NutritionFragment
import android.graphics.Bitmap
import com.jasonstudio.cookbook2.FavouriteRecipeModel.FavouriteRecipeViewModel
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.jasonstudio.cookbook2.R
import com.jasonstudio.cookbook2.RecipeActivity.ViewPagerAdapter
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.ToggleButton
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.jasonstudio.cookbook2.FavouriteRecipeModel.FavouriteRecipe
import com.google.android.material.snackbar.Snackbar
import androidx.fragment.app.FragmentPagerAdapter
import com.jasonstudio.cookbook2.RecipeActivity
import com.jasonstudio.cookbook2.ext.parcelable
import java.util.ArrayList

class RecipeActivity : AppCompatActivity() {
    //Toolbar UI objecct ---------------------------------------------------------------------------
    private val subImageUrl1 = "https://spoonacular.com/recipeImages/"
    private val subImageUrl2 = "-556x370.jpg"
    lateinit private var recipeTitleTextView: AppCompatTextView
    lateinit private var recipeImageView: ImageView

    //Toolbar UI objecct ---------------------------------------------------------------------------
    //Tab UI object---------------------------------------------------------------------------------
    lateinit private var viewPager: ViewPager
    lateinit private var tabLayout: TabLayout
    lateinit private var recipeInstructionFragment: RecipeInstructionFragment
    lateinit private var ingredientFragement: IngredientFragement
    lateinit private var nutritionFragment: NutritionFragment

    //Tab UI object---------------------------------------------------------------------------------
    //Data Object-----------------------------------------------------------------------------------
    lateinit private var recipe_id: String
    lateinit private var recipe_title: String
    lateinit private var recipe_image: Bitmap
    private var is_favourite_recipe = false
    lateinit private var fRViewModel: FavouriteRecipeViewModel

    //Data Object-----------------------------------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("onCreateRcipeActivity", "here")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)
        recipe_id = intent.getStringExtra("id") ?: ""
        recipe_title = intent.getStringExtra("title") ?: ""
        recipe_image = intent.parcelable("recipe_image")!!
        initializeToolBar()
        is_favourite_recipe = intent.getBooleanExtra("favourite_recipe", true)
        //Log.d("onCreate",recipe_id);
        viewPager = findViewById(R.id.view_pager)
        tabLayout = findViewById(R.id.tab_layout)
        val bundleIgd = bundleForIngredientFragment()
        recipeInstructionFragment = RecipeInstructionFragment()
        recipeInstructionFragment!!.arguments = bundleIgd
        ingredientFragement = IngredientFragement()
        ingredientFragement!!.arguments = bundleIgd
        nutritionFragment = NutritionFragment()
        nutritionFragment!!.arguments = bundleIgd
        tabLayout.setupWithViewPager(viewPager)
        val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager, 0)
        viewPagerAdapter.addFragment(ingredientFragement!!, "Ingredients")
        viewPagerAdapter.addFragment(recipeInstructionFragment!!, "Steps")
        viewPagerAdapter.addFragment(nutritionFragment!!, "Nutritions")
        viewPager.setAdapter(viewPagerAdapter)
        tabLayout.getTabAt(0)!!.setIcon(R.drawable.ic_ingredient)
        tabLayout.getTabAt(1)!!.setIcon(R.drawable.ic_recipe_instruction)
        tabLayout.getTabAt(2)!!.setIcon(R.drawable.ic_nutrition)

        /*
        for(int i=0; i<3; i++){
            tabLayout.getTabAt(i).getOrCreateBadge().setNumber(1);
        }*/fRViewModel = ViewModelProvider(this).get(
            FavouriteRecipeViewModel::class.java
        )
    }

    private fun initializeToolBar() {
        recipeTitleTextView = findViewById(R.id.recipe_title)
        recipeImageView = findViewById(R.id.recipe_image)
        recipeTitleTextView.setText(recipe_title)
        //recipeImageView.setImageBitmap(recipe_image);
        Glide.with(this).load(subImageUrl1 + recipe_id + subImageUrl2)
            .diskCacheStrategy(DiskCacheStrategy.ALL).into(recipeImageView)
        val toolbar = findViewById<Toolbar>(R.id.tool_bar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        supportActionBar!!.setTitle(recipe_title)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.recipe_information_menu, menu)
        val button =
            menu.findItem(R.id.menu_favourite_recipe).actionView?.findViewById<View>(R.id.menu_favourite_recipe_toggle_button) as ToggleButton
        button.isChecked = is_favourite_recipe
        button.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                val recipe = FavouriteRecipe(recipe_id, recipe_title)
                recipe.primaryKey = Integer.valueOf(recipe_id!!)
                fRViewModel!!.insert(recipe)
                Snackbar.make(
                    window.decorView,
                    "\'$recipe_title\' is added to the favourite recipe list.",
                    Snackbar.LENGTH_LONG
                ).show()
            } else {
                val recipe = FavouriteRecipe(recipe_id, recipe_title)
                recipe.primaryKey = Integer.valueOf(recipe_id!!)
                fRViewModel!!.delete(recipe)
                Snackbar.make(
                    window.decorView,
                    "\'$recipe_title\' is deleted to the favourite recipe list.",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private inner class ViewPagerAdapter(fm: FragmentManager, behavior: Int) :
        FragmentPagerAdapter(fm, behavior) {
        private val fragmentTitles: MutableList<String> = ArrayList()
        private val fragments: MutableList<Fragment> = ArrayList()
        fun addFragment(fragment: Fragment, title: String) {
            fragmentTitles.add(title)
            fragments.add(fragment)
        }

        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return fragmentTitles[position]
        }

        override fun getCount(): Int {
            return fragments.size
        }
    }

    private fun bundleForIngredientFragment(): Bundle {
        val bundle = Bundle()
        bundle.putString("recipe_id", recipe_id)
        /*recipeData = new HashMap<>();
        recipeData.put("ingredient_image_url", new ArrayList<String>());
        recipeData.put("ingredient_title", new ArrayList<String>());
        recipeData.put("ingredient_amount", new ArrayList<String>());
        requestQueue = Volley.newRequestQueue(this);
        loadRecipeDetails();
        bundle.putSerializable("recipeDataHashMap", recipeData);*/return bundle
    }

    companion object {
        private fun convertDecimalToFraction(x: Double): String {
            if (x < 0) {
                return "-" + convertDecimalToFraction(-x)
            }
            val tolerance = 1.0E-6
            var h1 = 1.0
            var h2 = 0.0
            var k1 = 0.0
            var k2 = 1.0
            var b = x
            do {
                val a = Math.floor(b)
                var aux = h1
                h1 = a * h1 + h2
                h2 = aux
                aux = k1
                k1 = a * k1 + k2
                k2 = aux
                b = 1 / (b - a)
            } while (Math.abs(x - h1 / k1) > x * tolerance)
            val h1result = h1.toInt()
            val k1result = k1.toInt()
            return if (k1result == 1) Integer.toString(h1result) else "$h1result/$k1result"
        }
    }
}