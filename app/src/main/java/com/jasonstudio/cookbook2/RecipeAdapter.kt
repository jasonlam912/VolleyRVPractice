package com.jasonstudio.cookbook2

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ToggleButton
import androidx.annotation.ColorRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.util.Pair
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.jasonstudio.cookbook2.FavouriteRecipeModel.FavouriteRecipe
import com.jasonstudio.cookbook2.FavouriteRecipeModel.FavouriteRecipeViewModel
import com.jasonstudio.cookbook2.Recipe.RecipeModel
import com.jasonstudio.cookbook2.view.activity.RecipeActivity

class RecipeAdapter(private val ct: Context, var recipeData: List<RecipeModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val fRViewModel: FavouriteRecipeViewModel

    init {
        fRViewModel = ViewModelProviders.of((ct as FragmentActivity)).get(
            FavouriteRecipeViewModel::class.java
        )
    }

    fun modifyData(recipeData: List<RecipeModel>) {
        this.recipeData = recipeData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(ct)
        val view: View
        when (viewType) {
            0 -> {
                view = inflater.inflate(R.layout.recipe_row, parent, false)
                return MyViewHolder(view)
            }
            1 -> {
                view = inflater.inflate(R.layout.row_loading, parent, false)
                return LoadViewHolder(view)
            }
            2 -> {
                view = inflater.inflate(R.layout.row_end, parent, false)
                return EndViewHolder(view)
            }
            3 -> {
                view = inflater.inflate(R.layout.recipe_pad_row, parent, false)
                return PaddingViewHolder(view)
            }
            else -> {
                view = inflater.inflate(R.layout.recipe_pad_row, parent, false)
                return PaddingViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, i: Int) {
        when (recipeData[i].statusForDisplay) {
            0 -> {
                val holder1 = holder as MyViewHolder
                holder1.titleText.text = recipeData.get(i).recipeTitle
                if (recipeData[i].recipeIamge != null) {
                    holder1.recipeImageView.setImageBitmap(recipeData[i].recipeIamge)
                } else {
                    holder1.recipeImageView.setImageDrawable(ct.resources.getDrawable(R.drawable.food_demo))
                }
                holder.favouriteButton.isChecked = recipeData.get(i).isfRIndicator()
            }
            1, 2, 3 -> {}
        }
    }

    override fun getItemViewType(position: Int): Int {
        return recipeData[position].statusForDisplay!!
    }

    override fun getItemCount(): Int {
        return recipeData.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var recipeImageView: ImageView
        var titleText: AppCompatTextView
        var favouriteButton: ToggleButton

        init {
            recipeImageView = itemView.findViewById(R.id.recipe_image)
            titleText = itemView.findViewById(R.id.recipe_title)
            //titleText.setAutoSizeTextTypeUniformWithConfiguration(8,24,2,1);
            favouriteButton = itemView.findViewById(R.id.favourite_toggle_button)
            favouriteButton.setOnCheckedChangeListener { buttonView, isChecked ->
                Log.d("onCheckedChanged", java.lang.Boolean.toString(isChecked))
                if (isChecked) {
                    val fR = FavouriteRecipe(
                        recipeData[adapterPosition].recipeId!!,
                        recipeData[adapterPosition].recipeTitle!!
                    )
                    recipeData[adapterPosition].setfRIndicator(true)
                    fR.primaryKey = recipeData.get(adapterPosition).recipeId!!.toInt()
                    fRViewModel.insert(fR)
                } else {
                    val key = recipeData[adapterPosition].recipeId!!.toInt()
                    recipeData[adapterPosition].setfRIndicator(false)
                    fRViewModel.deleteWithKey(key)
                }
                //notifyItemChanged(i);
                //modifyData(recipe_title,image_link,recipe_id,recipe_status,fRIndicators);
            }
            itemView.setOnClickListener {
                val intent = Intent(ct, RecipeActivity::class.java)
                val p1 = Pair.create(recipeImageView as View, recipeImageView.transitionName)
                val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    (ct as Activity), p1
                )
                intent.putExtra("id", recipeData[adapterPosition].recipeId)
                intent.putExtra("title", recipeData[adapterPosition].recipeTitle)
                intent.putExtra("favourite_recipe", favouriteButton.isChecked)
                //intent.putExtra("recipe_image", recipeData.get(getAdapterPosition()).getRecipeIamge());
                ct.startActivity(intent, optionsCompat.toBundle())
            }
        }
    }
}
class EndViewHolder(itemView: View, @ColorRes color: Int = R.color.app_background) : RecyclerView.ViewHolder(itemView) {
    init {
        val view = itemView.findViewById<View>(R.id.cl_parent)
        view.setBackgroundColor(ResourcesCompat.getColor(view.resources, color, null))
    }
}
class LoadViewHolder(itemView: View, @ColorRes color: Int = R.color.app_background) : RecyclerView.ViewHolder(itemView) {
    init {
        val view = itemView.findViewById<View>(R.id.cl_parent)
        view.setBackgroundColor(ResourcesCompat.getColor(view.resources, color, null))
    }
}
class PaddingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)