package com.jasonstudio.cookbook2.view.IngredientClasses

import android.content.Context
import com.jasonstudio.cookbook2.Network.NetworkManager.Companion.getInstance
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import com.jasonstudio.cookbook2.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import android.widget.TextView
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import java.util.HashMap

class IngredientAdapter(
    private val ct: Context?,
    private var igdData: HashMap<String, MutableList<String>>?,
    private val recipeId: String?
) : RecyclerView.Adapter<IngredientAdapter.ViewHolder>() {
    fun ModifyData(igdData: HashMap<String, MutableList<String>>?) {
        this.igdData = igdData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(ct)
        val itemView = inflater.inflate(R.layout.ingredient_row, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.ingredientTitle.text = igdData!!["ingredient_title"]!![position]
        holder.ingredientAmount.text = igdData!!["ingredient_amount"]!![position]
        Glide.with(ct!!)
            .load(igdData!!["ingredient_image_url"]!![position])
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.ingredientImage)
    }

    override fun getItemCount(): Int {
        return igdData!!["ingredient_title"]!!.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ingredientTitle: TextView
        var ingredientAmount: TextView
        var ingredientImage: ImageView

        init {
            ingredientTitle = itemView.findViewById(R.id.ingredient_name)
            ingredientAmount = itemView.findViewById(R.id.ingredient_amount)
            ingredientImage = itemView.findViewById(R.id.ingredient_image)
        }
    }
}