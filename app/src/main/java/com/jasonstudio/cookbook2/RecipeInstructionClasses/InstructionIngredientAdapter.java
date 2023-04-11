package com.jasonstudio.cookbook2.RecipeInstructionClasses;

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
import com.jasonstudio.cookbook2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class InstructionIngredientAdapter extends RecyclerView.Adapter<InstructionIngredientAdapter.ViewHolder> {
    private Context ct;
    private JSONArray igdList;
    public InstructionIngredientAdapter(Context ct, JSONArray igdList){
        this.ct = ct;
        this.igdList = igdList;
    }
    @NonNull
    @Override
    public InstructionIngredientAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ct);
        View view = inflater.inflate(R.layout.instruction_ingredient_column, parent,false);
        return new InstructionIngredientAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InstructionIngredientAdapter.ViewHolder holder, int position) {
        try {
            JSONObject igdData = (JSONObject)igdList.get(position);
            String igdTitleString = igdData.getString("name");
            String igdImageUrl = "https://spoonacular.com/cdn/ingredients_250x250/"+igdData.getString("image");
            holder.igdTitle.setText(igdTitleString);
            Glide.with(ct)
                    .load(igdImageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.igdImage);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return igdList.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView igdTitle;
        ImageView igdImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            igdTitle = itemView.findViewById(R.id.instruction_ingredient_title);
            igdImage =itemView.findViewById(R.id.instruction_ingredient_image);
        }
    }
}
