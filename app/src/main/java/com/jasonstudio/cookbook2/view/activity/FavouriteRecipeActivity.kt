package com.jasonstudio.cookbook2.view.activity

import androidx.appcompat.app.AppCompatActivity
import com.jasonstudio.cookbook2.FavouriteRecipeModel.FavouriteRecipeViewModel
import androidx.recyclerview.widget.RecyclerView
import com.jasonstudio.cookbook2.RecipeAdapter
import com.jasonstudio.cookbook2.R
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.jasonstudio.cookbook2.Recipe.RecipeModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.lifecycle.ViewModelProvider
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers
import com.jasonstudio.cookbook2.Recipe.JsonData2Recipe
import java.util.ArrayList

class FavouriteRecipeActivity : AppCompatActivity() {
    private lateinit var fRViewModel: FavouriteRecipeViewModel
    private lateinit var rv: RecyclerView
    private lateinit var adapter: RecipeAdapter
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.favourite_recipe_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.delete_all_recipe) {
            fRViewModel!!.deleteAll()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite_recipe)
        title = "Favourite Recipes"
        rv = findViewById(R.id.favourite_recipe_rv)
        adapter = RecipeAdapter(this, ArrayList())
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(
            this@FavouriteRecipeActivity,
            RecyclerView.VERTICAL,
            false
        )
        fRViewModel = ViewModelProvider(this).get(FavouriteRecipeViewModel::class.java)
        val d = fRViewModel.all?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({ favouriteRecipes ->
                val recipeList: MutableList<RecipeModel> = ArrayList()
                for (i in favouriteRecipes.indices) {
                    val recipe = favouriteRecipes[i]
                    val url =
                        JsonData2Recipe.subImageUrl1 + recipe.id + JsonData2Recipe.subImageUrl2
                    Log.d(TAG, url)
                    val r = RecipeModel(recipe.id, recipe.title, url, 0, true, null)
                    recipeList.add(r)
                }
                adapter!!.modifyData(recipeList)
            }, { throwable -> throwable.printStackTrace() })
    }

    companion object {
        private val TAG = FavouriteRecipeActivity::class.java.name
    }
}