package com.jasonstudio.cookbook2.view.IngredientClasses

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.jasonstudio.cookbook2.R
import com.jasonstudio.cookbook2.ext.addInterceptor

class IngredientAdapter(
    private val ct: Context?,
    private var igdData: HashMap<String, MutableList<String>>?,
) : RecyclerView.Adapter<IngredientAdapter.ViewHolder>() {
    fun modifyData(igdData: HashMap<String, MutableList<String>>?) {
        this.igdData = igdData
        if (igdData != null) {
            notifyItemRangeChanged(0,igdData.size)
        }
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
            .addInterceptor()
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