package com.jasonstudio.cookbook2.RecipeInstructionClasses

import android.content.Context
import org.json.JSONArray
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import com.jasonstudio.cookbook2.R
import org.json.JSONObject
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import org.json.JSONException
import android.widget.TextView

class InstructionEquipmentAdapter(private val ct: Context, private val eqmList: JSONArray) :
    RecyclerView.Adapter<InstructionEquipmentAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(ct)
        val view = inflater.inflate(R.layout.instruction_equipment_column, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            val eqmData = eqmList[position] as JSONObject
            val eqmTitleString = eqmData.getString("name")
            val eqmImageUrl =
                "https://spoonacular.com/cdn/equipment_250x250/" + eqmData.getString("image")
            holder.eqmTitle.text = eqmTitleString
            Glide.with(ct)
                .load(eqmImageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.eqmImage)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return eqmList.length()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var eqmTitle: TextView
        var eqmImage: ImageView

        init {
            eqmTitle = itemView.findViewById(R.id.instruction_equipment_title)
            eqmImage = itemView.findViewById(R.id.instruction_equipment_image)
        }
    }
}