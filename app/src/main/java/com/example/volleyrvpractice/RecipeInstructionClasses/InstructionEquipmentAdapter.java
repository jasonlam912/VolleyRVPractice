package com.example.volleyrvpractice.RecipeInstructionClasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.volleyrvpractice.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class InstructionEquipmentAdapter extends RecyclerView.Adapter<InstructionEquipmentAdapter.ViewHolder> {

    private Context ct;
    private JSONArray eqmList;
    public InstructionEquipmentAdapter(Context ct, JSONArray eqmList){
        this.ct = ct;
        this.eqmList = eqmList;
    }

    @NonNull
    @Override
    public InstructionEquipmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ct);
        View view = inflater.inflate(R.layout.instruction_equipment_column,parent,false);
        return new InstructionEquipmentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InstructionEquipmentAdapter.ViewHolder holder, int position) {
        try {
            JSONObject eqmData = (JSONObject)eqmList.get(position);
            String eqmTitleString = eqmData.getString("name");
            String eqmImageUrl = "https://spoonacular.com/cdn/equipment_250x250/"+eqmData.getString("image");
            holder.eqmTitle.setText(eqmTitleString);
            Glide.with(ct)
                    .load(eqmImageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.eqmImage);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return eqmList.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView eqmTitle;
        ImageView eqmImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eqmTitle = itemView.findViewById(R.id.instruction_equipment_title);
            eqmImage = itemView.findViewById(R.id.instruction_equipment_image);
        }
    }
}
