package com.jasonstudio.cookbook2.IngredientClasses;

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

import java.util.HashMap;
import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {

    private Context ct;
    private HashMap<String,List<String>> igdData;
    private String recipeId;


    public IngredientAdapter(Context context, HashMap<String, List<String>> igdData, String recipeId){
        ct = context;
        this.igdData = igdData;
        this.recipeId = recipeId;
    }

    public void ModifyData(HashMap<String,List<String>> igdData){
        this.igdData = igdData;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public IngredientAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ct);
        View itemView = inflater.inflate(R.layout.ingredient_row, parent, false);

        return new IngredientAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientAdapter.ViewHolder holder, int position) {
        holder.ingredientTitle.setText(igdData.get("ingredient_title").get(position));
        holder.ingredientAmount.setText(igdData.get("ingredient_amount").get(position));
        Glide.with(ct)
                .load(igdData.get("ingredient_image_url").get(position))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.ingredientImage);
        //Log.d("igdAdaOnBindviewHolder",igdData.get("ingredient_title").get(position));

    }

    @Override
    public int getItemCount() {
        return igdData.get("ingredient_title").size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView ingredientTitle, ingredientAmount;
        ImageView ingredientImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientTitle = itemView.findViewById(R.id.ingredient_name);
            ingredientAmount = itemView.findViewById(R.id.ingredient_amount);
            ingredientImage = itemView.findViewById(R.id.ingredient_image);
        }
    }
}
