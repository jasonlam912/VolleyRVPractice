package com.jasonstudio.cookbook2.view.activity

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.view.ViewGroup.MarginLayoutParams
import android.widget.ToggleButton
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.jasonstudio.cookbook2.FavouriteRecipeModel.FavouriteRecipe
import com.jasonstudio.cookbook2.FavouriteRecipeModel.FavouriteRecipeViewModel
import com.jasonstudio.cookbook2.R
import com.jasonstudio.cookbook2.databinding.ActivityRecipeBinding
import com.jasonstudio.cookbook2.ext.addInterceptor
import com.jasonstudio.cookbook2.ext.hideStatusBar
import com.jasonstudio.cookbook2.ext.showStatusBar
import com.jasonstudio.cookbook2.ext.toPx
import com.jasonstudio.cookbook2.model.Video
import com.jasonstudio.cookbook2.view.IngredientClasses.IngredientFragement
import com.jasonstudio.cookbook2.view.RecipeInstructionClasses.RecipeInstructionFragment
import com.jasonstudio.cookbook2.view.RecipeNutritionClasses.NutritionFragment
import com.jasonstudio.cookbook2.view.VideoListFragment
import com.jasonstudio.cookbook2.view.videos.OnVideoClickListener
import com.jasonstudio.cookbook2.view.videos.OnVideoFragmentListener
import com.jasonstudio.cookbook2.view.videos.VideoFragment

class RecipeActivity : BaseActivity<ActivityRecipeBinding>(ActivityRecipeBinding::inflate), OnVideoClickListener, OnVideoFragmentListener {
    //Toolbar UI objecct ---------------------------------------------------------------------------
    private val subImageUrl1 = "https://spoonacular.com/recipeImages/"
    private val subImageUrl2 = "-556x370.jpg"
    //Toolbar UI objecct ---------------------------------------------------------------------------
    //Tab UI object---------------------------------------------------------------------------------
    private var fragmentTitles: MutableList<String> = ArrayList()
    private lateinit var recipeInstructionFragment: RecipeInstructionFragment
    private lateinit var ingredientFragement: IngredientFragement
    private lateinit var nutritionFragment: NutritionFragment

    //Tab UI object---------------------------------------------------------------------------------
    //Data Object-----------------------------------------------------------------------------------
    private lateinit var recipe_id: String
    private lateinit var recipe_title: String
    private var is_favourite_recipe = false
    private lateinit var fRViewModel: FavouriteRecipeViewModel
    //Data Object-----------------------------------------------------------------------------------

    private var isVideoExpand = false
    set(value) {
        field = value
        setVideoFragment(field)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("onCreateRcipeActivity", "here")
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        recipe_id = intent.getStringExtra("id") ?: ""
        recipe_title = intent.getStringExtra("title") ?: ""
        initializeToolBar()
        is_favourite_recipe = intent.getBooleanExtra("favourite_recipe", true)

        val bundleIgd = bundleForIngredientFragment()
        recipeInstructionFragment = RecipeInstructionFragment()
        recipeInstructionFragment.arguments = bundleIgd
        ingredientFragement = IngredientFragement()
        ingredientFragement.arguments = bundleIgd
        nutritionFragment = NutritionFragment()
        nutritionFragment.arguments = bundleIgd
        val videoFragment = VideoListFragment.getInstance()
        videoFragment.onVideoClickListener = this
        videoFragment.arguments = bundleIgd
        videoFragment.arguments?.putString("title", recipe_title)
        fragmentTitles = arrayListOf(
            "Ingredients",
            "Steps",
            "Related Videos",
            "Nutritions",
        )


        binding.layoutRecipe.viewPager.apply {
            val viewPagerAdapter = ViewPagerAdapter(this@RecipeActivity)
            viewPagerAdapter.addFragment(ingredientFragement)
            viewPagerAdapter.addFragment(recipeInstructionFragment)
            viewPagerAdapter.addFragment(videoFragment)
            viewPagerAdapter.addFragment(nutritionFragment)
            adapter = viewPagerAdapter
        }

        TabLayoutMediator(
            binding.layoutRecipe.tabLayout,
            binding.layoutRecipe.viewPager
        ) { tab, pos ->
            tab.text = fragmentTitles[pos]
        }.attach()

        binding.layoutRecipe.tabLayout.apply {
            this.getTabAt(0)!!.setIcon(R.drawable.ic_ingredient)
            this.getTabAt(1)!!.setIcon(R.drawable.ic_recipe_instruction)
            this.getTabAt(3)!!.setIcon(R.drawable.ic_nutrition)
            this.getTabAt(2)!!.setIcon(R.drawable.ic_videos)
        }

        fRViewModel = ViewModelProvider(this).get(
            FavouriteRecipeViewModel::class.java
        )
        isVideoExpand = false
    }

    private fun setVideoFragment(isExpand: Boolean) {
        val duration = 500L
//        val expandCS = ConstraintSet()
//        val collapseCS = ConstraintSet()
//        expandCS.clone(binding.clRecipe)
//        collapseCS.clone(binding.clRecipe)
//        expandCS.clear(binding.flVideo.id)
//        collapseCS.clear(binding.flVideo.id)
//        expandCS.apply {
//            connect(binding.flVideo.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
//            connect(binding.flVideo.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
//            connect(binding.flVideo.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
//            connect(binding.flVideo.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
//        }
//        collapseCS.apply {
//            connect(binding.flVideo.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
//            connect(binding.flVideo.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
//        }
//        val newLayout: ConstraintSet = if (isExpand) expandCS else collapseCS
//        val transition: Transition = ChangeBounds()
//        transition.interpolator = DecelerateInterpolator()
//        transition.duration = duration
//        TransitionManager.beginDelayedTransition(binding.clRecipe, transition)
//        newLayout.applyTo(binding.clRecipe)
//        val expandAnim = AnimProperty(
//            0,0,0,0,-1,-1
//        )
//        val collapseAnim = AnimProperty(
//            0,10,0,10,200,112
//        )
//        binding.flVideo.valueAnimate(
//            duration,
//            if (isExpand) collapseAnim else expandAnim,
//            if (isExpand) expandAnim else collapseAnim,
//        )
        requestedOrientation = if (isExpand) ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE else ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        (binding.flVideo.layoutParams as MarginLayoutParams).apply {
            bottomMargin = if (isExpand) 0 else 10.toPx(this@RecipeActivity)
            marginEnd = if (isExpand) 0 else 10.toPx(this@RecipeActivity)
            width = if (isExpand) LayoutParams.MATCH_PARENT else 200.toPx(this@RecipeActivity)
            height = if (isExpand) LayoutParams.MATCH_PARENT else 112.toPx(this@RecipeActivity)
        }
        if (isExpand) {
            window.hideStatusBar()
        } else {
            window.showStatusBar()
        }
    }
    private fun initializeToolBar() {
        binding.layoutRecipe.recipeTitle.apply {
            text = recipe_title
        }
        binding.layoutRecipe.recipeImage.apply {
            Glide.with(this).load(subImageUrl1 + recipe_id + subImageUrl2)
                .addInterceptor()
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(this)
        }
        binding.layoutRecipe.toolBar.apply {
            setSupportActionBar(this)
        }
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.title = recipe_title
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.recipe_information_menu, menu)
        val button =
            menu.findItem(R.id.menu_favourite_recipe).actionView?.findViewById<View>(R.id.menu_favourite_recipe_toggle_button) as ToggleButton
        button.isChecked = is_favourite_recipe
        button.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                val recipe = FavouriteRecipe(recipe_id, recipe_title)
                recipe.primaryKey = Integer.valueOf(recipe_id)
                fRViewModel.insert(recipe)
                Snackbar.make(
                    window.decorView,
                    "\'$recipe_title\' is added to the favourite recipe list.",
                    Snackbar.LENGTH_LONG
                ).show()
            } else {
                val recipe = FavouriteRecipe(recipe_id, recipe_title)
                recipe.primaryKey = Integer.valueOf(recipe_id)
                fRViewModel.delete(recipe)
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

    override fun onVideoClicked(video: Video) {
        isVideoExpand = false
        val fragment = VideoFragment.newInstance(video.youTubeID)
        fragment.onVideoFragmentListener = this
        supportFragmentManager.commit {
            addToBackStack(null)
            add(binding.flVideo.id, fragment)
        }
        onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                supportFragmentManager.popBackStack()
                window.showStatusBar()
                isVideoExpand = false
                isEnabled = false
            }
        })
    }

    override fun onExpandBtnClicked() {
        isVideoExpand = !isVideoExpand
    }

    override fun onCloseBtnClicked() {
        onBackPressedDispatcher.onBackPressed()
    }
}