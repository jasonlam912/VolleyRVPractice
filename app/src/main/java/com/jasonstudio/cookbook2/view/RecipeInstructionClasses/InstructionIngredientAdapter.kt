package com.jasonstudio.cookbook2.view.RecipeInstructionClasses

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
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class InstructionIngredientAdapter(private val ct: Context, private val igdList: JSONArray) :
    RecyclerView.Adapter<InstructionIngredientAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(ct)
        val view = inflater.inflate(R.layout.instruction_ingredient_column, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            val igdData = igdList[position] as JSONObject
            val igdTitleString = igdData.getString("name")
            val igdImageUrl =
                "https://spoonacular.com/cdn/ingredients_250x250/" + igdData.getString("image")
            holder.igdTitle.text = igdTitleString
            Glide.with(ct)
                .load(igdImageUrl)
                .addInterceptor()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.igdImage)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return igdList.length()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var igdTitle: TextView
        var igdImage: ImageView

        init {
            igdTitle = itemView.findViewById(R.id.instruction_ingredient_title)
            igdImage = itemView.findViewById(R.id.instruction_ingredient_image)
        }
    }
}