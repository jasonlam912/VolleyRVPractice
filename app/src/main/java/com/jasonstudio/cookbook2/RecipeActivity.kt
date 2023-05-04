package com.jasonstudio.cookbook2

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import com.google.android.material.tabs.TabLayout
import com.jasonstudio.cookbook2.view.RecipeInstructionClasses.RecipeInstructionFragment
import com.jasonstudio.cookbook2.view.IngredientClasses.IngredientFragement
import com.jasonstudio.cookbook2.view.RecipeNutritionClasses.NutritionFragment
import com.jasonstudio.cookbook2.FavouriteRecipeModel.FavouriteRecipeViewModel
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import android.widget.ImageView
import android.widget.ToggleButton
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.jasonstudio.cookbook2.FavouriteRecipeModel.FavouriteRecipe
import com.google.android.material.snackbar.Snackbar
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.jasonstudio.cookbook2.view.VideoListFragment
import java.util.ArrayList

class RecipeActivity : AppCompatActivity() {
    //Toolbar UI objecct ---------------------------------------------------------------------------
    private val subImageUrl1 = "https://spoonacular.com/recipeImages/"
    private val subImageUrl2 = "-556x370.jpg"
    lateinit private var recipeTitleTextView: AppCompatTextView
    lateinit private var recipeImageView: ImageView

    //Toolbar UI objecct ---------------------------------------------------------------------------
    //Tab UI object---------------------------------------------------------------------------------
    lateinit private var viewPager: ViewPager2
    lateinit private var tabLayout: TabLayout
    private var fragmentTitles: MutableList<String> = ArrayList()
    lateinit private var recipeInstructionFragment: RecipeInstructionFragment
    lateinit private var ingredientFragement: IngredientFragement
    lateinit private var nutritionFragment: NutritionFragment

    //Tab UI object---------------------------------------------------------------------------------
    //Data Object-----------------------------------------------------------------------------------
    lateinit private var recipe_id: String
    lateinit private var recipe_title: String
    private var is_favourite_recipe = false
    lateinit private var fRViewModel: FavouriteRecipeViewModel

    //Data Object-----------------------------------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("onCreateRcipeActivity", "here")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)
        recipe_id = intent.getStringExtra("id") ?: ""
        recipe_title = intent.getStringExtra("title") ?: ""
        initializeToolBar()
        is_favourite_recipe = intent.getBooleanExtra("favourite_recipe", true)

        viewPager = findViewById(R.id.view_pager)
        tabLayout = findViewById(R.id.tab_layout)
        val bundleIgd = bundleForIngredientFragment()
        recipeInstructionFragment = RecipeInstructionFragment()
        recipeInstructionFragment.arguments = bundleIgd
        ingredientFragement = IngredientFragement()
        ingredientFragement.arguments = bundleIgd
        nutritionFragment = NutritionFragment()
        nutritionFragment.arguments = bundleIgd
        val videoFragment = VideoListFragment()
        videoFragment.arguments = bundleIgd
        videoFragment.arguments?.putString("title", recipe_title)
        fragmentTitles = arrayListOf(
            "Ingredients",
            "Steps",
            "Related Videos",
            "Nutritions",
        )
        val viewPagerAdapter = ViewPagerAdapter(this)
        viewPagerAdapter.addFragment(ingredientFragement,)
        viewPagerAdapter.addFragment(recipeInstructionFragment,)
        viewPagerAdapter.addFragment(videoFragment)
        viewPagerAdapter.addFragment(nutritionFragment,)
        viewPager.adapter = viewPagerAdapter
        TabLayoutMediator(tabLayout, viewPager) { tab, pos ->
            tab.text = fragmentTitles[pos]
        }.attach()
        tabLayout.getTabAt(0)!!.setIcon(R.drawable.ic_ingredient)
        tabLayout.getTabAt(1)!!.setIcon(R.drawable.ic_recipe_instruction)
        tabLayout.getTabAt(3)!!.setIcon(R.drawable.ic_nutrition)
        tabLayout.getTabAt(2)!!.setIcon(R.drawable.ic_videos)

        fRViewModel = ViewModelProvider(this).get(
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

    private inner class ViewPagerAdapter(fa: FragmentActivity) :
        FragmentStateAdapter(fa) {
        private val fragments: MutableList<Fragment> = ArrayList()
        fun addFragment(fragment: Fragment) {
            fragments.add(fragment)
        }

        override fun getItemCount(): Int {
            return fragments.size
        }

        override fun createFragment(position: Int): Fragment {
            return fragments[position]
        }
    }

    private fun bundleForIngredientFragment(): Bundle {
        val bundle = Bundle()
        bundle.putString("recipe_id", recipe_id)
        return bundle
    }
}