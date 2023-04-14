package com.example.volleyrvpractice.RecipeInstructionClasses

import android.content.Context
import org.json.JSONArray
import androidx.recyclerview.widget.RecyclerView
import com.example.volleyrvpractice.RecipeInstructionClasses.InstructionIngredientAdapter
import com.example.volleyrvpractice.RecipeInstructionClasses.InstructionEquipmentAdapter
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.example.volleyrvpractice.R
import org.json.JSONObject
import androidx.recyclerview.widget.LinearLayoutManager
import org.json.JSONException
import android.widget.TextView
import androidx.cardview.widget.CardView
import java.util.ArrayList

class InstructionAdapter(private val ct: Context, var data: JSONArray) :
    RecyclerView.Adapter<InstructionAdapter.ViewHolder>() {
    var igdAdapters: MutableList<InstructionIngredientAdapter>
    var eqmAdapters: MutableList<InstructionEquipmentAdapter>
    fun modifyData(data: JSONArray) {
        this.data = data
        notifyDataSetChanged()
    }

    init {
        igdAdapters = ArrayList()
        eqmAdapters = ArrayList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(ct)
        val view = inflater.inflate(R.layout.instruction_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            val steps = (data[0] as JSONObject).getJSONArray("steps")
            val stepData = steps[position] as JSONObject
            val step_content = stepData.getString("step")
            val igdList = stepData.getJSONArray("ingredients")
            val eqmList = stepData.getJSONArray("equipment")
            val step_title = "Step " + (position + 1)
            holder.instructionTitle.text = step_title
            holder.instructionContent.text = step_content
            val igdRvManager = LinearLayoutManager(ct, RecyclerView.HORIZONTAL, false)
            val eqmRvManager = LinearLayoutManager(ct, RecyclerView.HORIZONTAL, false)
            igdAdapters.add(InstructionIngredientAdapter(ct, igdList))
            eqmAdapters.add(InstructionEquipmentAdapter(ct, eqmList))
            holder.ingredientRv.adapter = igdAdapters[position]
            holder.equipmentRv.adapter = eqmAdapters[position]
            holder.ingredientRv.layoutManager = igdRvManager
            holder.equipmentRv.layoutManager = eqmRvManager
            if (igdList.length() == 0) {
                holder.ingredientCardView.visibility = View.GONE
            }
            if (eqmList.length() == 0) {
                holder.equipmentCardView.visibility = View.GONE
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return try {
            //Log.d("Weeee", ((JSONObject)data.get(0)).getJSONArray("steps").toString());
            (data[0] as JSONObject).getJSONArray("steps").length()
        } catch (e: JSONException) {
            e.printStackTrace()
            0
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var instructionTitle: TextView
        var instructionContent: TextView
        var ingredientRv: RecyclerView
        var equipmentRv: RecyclerView
        var ingredientCardView: CardView
        var equipmentCardView: CardView

        init {
            instructionTitle = itemView.findViewById(R.id.instruction_title)
            instructionContent = itemView.findViewById(R.id.instruction)
            ingredientRv = itemView.findViewById(R.id.instruction_ingredient_rv)
            equipmentRv = itemView.findViewById(R.id.instruction_equipment_rv)
            ingredientCardView = itemView.findViewById(R.id.card_view_ingredient)
            equipmentCardView = itemView.findViewById(R.id.card_view_equipment)
        }
    }
}