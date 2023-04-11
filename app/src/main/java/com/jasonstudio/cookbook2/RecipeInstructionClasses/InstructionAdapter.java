package com.jasonstudio.cookbook2.RecipeInstructionClasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jasonstudio.cookbook2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class InstructionAdapter extends RecyclerView.Adapter<InstructionAdapter.ViewHolder> {
    public JSONArray data;
    private Context ct;
    public List<InstructionIngredientAdapter> igdAdapters;
    public List<InstructionEquipmentAdapter> eqmAdapters;

    public void modifyData(JSONArray data){
        this.data = data;
        this.notifyDataSetChanged();
    }

    public InstructionAdapter(Context ct, JSONArray data){
        this.ct = ct;
        this.data = data;
        igdAdapters = new ArrayList<>();
        eqmAdapters = new ArrayList<>();
    }
    @NonNull
    @Override
    public InstructionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ct);
        View view = inflater.inflate(R.layout.instruction_row,parent,false);
        return new InstructionAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InstructionAdapter.ViewHolder holder, int position) {
        try {
            JSONArray steps = ((JSONObject)data.get(0)).getJSONArray("steps");
            JSONObject stepData = (JSONObject) steps.get(position);
            String step_content = stepData.getString("step");
            JSONArray igdList = stepData.getJSONArray("ingredients");
            JSONArray eqmList = stepData.getJSONArray("equipment");
            String step_title = "Step "+ (position+1);

            holder.instructionTitle.setText(step_title);
            holder.instructionContent.setText(step_content);
            LinearLayoutManager igdRvManager = new LinearLayoutManager(ct, RecyclerView.HORIZONTAL, false);
            LinearLayoutManager eqmRvManager = new LinearLayoutManager(ct, RecyclerView.HORIZONTAL, false);
            igdAdapters.add(new InstructionIngredientAdapter(ct, igdList));
            eqmAdapters.add(new InstructionEquipmentAdapter(ct, eqmList));
            holder.ingredientRv.setAdapter(igdAdapters.get(position));
            holder.equipmentRv.setAdapter(eqmAdapters.get(position));
            holder.ingredientRv.setLayoutManager(igdRvManager);
            holder.equipmentRv.setLayoutManager(eqmRvManager);
            if(igdList.length()==0){
                holder.ingredientCardView.setVisibility(View.GONE);
            }
            if(eqmList.length()==0){
                holder.equipmentCardView.setVisibility(View.GONE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        try {
            //Log.d("Weeee", ((JSONObject)data.get(0)).getJSONArray("steps").toString());
            return ((JSONObject)data.get(0)).getJSONArray("steps").length();
        } catch (JSONException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView instructionTitle, instructionContent;
        RecyclerView ingredientRv, equipmentRv;
        CardView ingredientCardView, equipmentCardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            instructionTitle = itemView.findViewById(R.id.instruction_title);
            instructionContent = itemView.findViewById(R.id.instruction);
            ingredientRv = itemView.findViewById(R.id.instruction_ingredient_rv);
            equipmentRv = itemView.findViewById(R.id.instruction_equipment_rv);
            ingredientCardView = itemView.findViewById(R.id.card_view_ingredient);
            equipmentCardView = itemView.findViewById(R.id.card_view_equipment);
        }
    }
}
