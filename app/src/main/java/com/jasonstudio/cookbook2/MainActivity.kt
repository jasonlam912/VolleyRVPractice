package com.example.volleyrvpractice

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.volleyrvpractice.RecipeAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.volleyrvpractice.Recipe.RecipeViewModel
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import android.os.Bundle
import com.example.volleyrvpractice.R
import com.example.volleyrvpractice.MainActivity
import com.example.volleyrvpractice.Recipe.RecipeModel
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import androidx.core.view.GravityCompat
import android.content.Intent
import android.content.res.Configuration
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.SearchView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import com.example.volleyrvpractice.FavouriteRecipe.FavouriteRecipeActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    lateinit var rv: RecyclerView
    lateinit var adapter: RecipeAdapter
    lateinit var manager: LinearLayoutManager
    lateinit private var recipeViewModel: RecipeViewModel
    lateinit private var drawerLayout: DrawerLayout
    lateinit private var nv: NavigationView
    lateinit private var toolbar: Toolbar

    //volley things
    lateinit private var loadingRecipePB: ProgressBar
    lateinit private var swipeRecipeRVContainer: SwipeRefreshLayout

    companion object {
        var searchString: String? = null
        private var orientation = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppThemeMain)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeDrawerLayout()
        orientation = resources.configuration.orientation
        loadingRecipePB = findViewById(R.id.loading_recipe_progress_bar)
        loadingRecipePB.setVisibility(View.INVISIBLE)
        swipeRecipeRVContainer = findViewById(R.id.swipe_recipe_rv_container)
        rv = findViewById(R.id.recipe_rv)

        adapter = RecipeAdapter(this, ArrayList())
        rv.setAdapter(adapter)
        manager = getManager(orientation)
        rv.setLayoutManager(manager)
        rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lastVisiblePosition = manager!!.findLastVisibleItemPosition()
                //Log.d("lastPos", Boolean.toString(lastVisiblePosition == recipeViewModel.getData().getValue().size()-1));
                if (!recipeViewModel!!.isLoading.value!! && lastVisiblePosition == recipeViewModel!!.data.value!!.size - 1) {
                    //Log.d("isSearch", Boolean.toString(recipeViewModel.isSearch));
                    recipeViewModel!!.putRecipe(this@MainActivity, searchString)
                }
            }
        }
        )
        recipeViewModel = ViewModelProvider(this).get(RecipeViewModel::class.java)
        recipeViewModel!!.data.observe(this) { recipeModels -> adapter!!.modifyData(recipeModels) }
        swipeRecipeRVContainer.setOnRefreshListener(OnRefreshListener {
            recipeViewModel!!.resetData(this@MainActivity)
            recipeViewModel!!.putRandomRecipe(this@MainActivity)
        })
        recipeViewModel!!.isLoading.observe(this) { aBoolean ->
            swipeRecipeRVContainer.setRefreshing(aBoolean!!)
            /*if(aBoolean){
                        loadingRecipePB.setVisibility(View.VISIBLE);
                    }else{
                        loadingRecipePB.setVisibility(View.INVISIBLE);
                    }*/
        }
        if (recipeViewModel!!.data.value!!.size == 2) {
            recipeViewModel!!.putRandomRecipe(this@MainActivity)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.refresh_menu, menu)
        val searchView = findViewById<View>(R.id.recipe_search_view) as SearchView
        searchView.maxWidth = Int.MAX_VALUE
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchString = query
                Log.d("onQueryTextSubmit", query)
                recipeViewModel!!.putSearchRecipe(this@MainActivity, query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    private fun initializeDrawerLayout() {
        drawerLayout = findViewById(R.id.drawer_layout)
        nv = findViewById(R.id.navigation_view_drawer)
        toolbar = findViewById(R.id.toolbar2)
        //toolbar.setNavigationIcon(R.drawable.recipes);
        toolbar.setTitle("")
        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.open_drawer,
            R.string.close_drawer
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        nv.setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener { item ->
            drawerLayout.closeDrawer(GravityCompat.START)
            when (item.itemId) {
                R.id.menu_find_recipe ->                         //item.setChecked(true);
                    return@OnNavigationItemSelectedListener true
                R.id.menu_favourite_recipe -> {
                    //item.setChecked(true);
                    startActivity(Intent(this@MainActivity, FavouriteRecipeActivity::class.java))
                    return@OnNavigationItemSelectedListener true
                }
                R.id.menu_about_us ->                         //item.setChecked(true);
                    return@OnNavigationItemSelectedListener true
            }
            false
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.refresh_recipe_button) {
            recipeViewModel!!.resetData(this)
            recipeViewModel!!.putRandomRecipe(this@MainActivity)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getManager(orientation: Int): LinearLayoutManager {
        return if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            LinearLayoutManager(this)
        } else {
            val manager = GridLayoutManager(this, 2)
            manager.spanSizeLookup = object : SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (adapter!!.getItemViewType(position) == 0) 1 else 2
                }
            }
            manager
        }
    }
}