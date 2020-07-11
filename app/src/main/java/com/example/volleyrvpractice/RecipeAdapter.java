package com.example.volleyrvpractice;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.volleyrvpractice.FavouriteRecipeModel.FavouriteRecipe;
import com.example.volleyrvpractice.FavouriteRecipeModel.FavouriteRecipeViewModel;
import com.example.volleyrvpractice.Recipe.RecipeModel;
import com.google.android.material.shape.RoundedCornerTreatment;

import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter {

    private List<RecipeModel> recipeData;
    private Context ct;
    private FavouriteRecipeViewModel fRViewModel;


    public RecipeAdapter(Context ct, List<RecipeModel> recipeData){
        this.ct = ct;
        this.recipeData = recipeData;
        fRViewModel = ViewModelProviders.of((FragmentActivity)ct).get(FavouriteRecipeViewModel.class);
    }

    public void modifyData(List<RecipeModel> recipeData){
        this.recipeData = recipeData;
        this.notifyDataSetChanged();
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ct);
        View view;
        switch (viewType){
            case 0:
                view = inflater.inflate(R.layout.recipe_row, parent, false);
                return new MyViewHolder(view);
            case 1:
                view = inflater.inflate(R.layout.loading_row, parent, false);
                return new LoadViewHolder(view);
            case 2:
                view = inflater.inflate(R.layout.end_row, parent, false);
                return new EndViewHolder(view);
            case 3:
                view = inflater.inflate(R.layout.recipe_pad_row, parent, false);
                return new PaddingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int i) {
        switch (recipeData.get(i).getStatusForDisplay()){
            case 0:
                ((RecipeAdapter.MyViewHolder)holder).titleText.setText(recipeData.get(i).getRecipeTitle());
                ((RecipeAdapter.MyViewHolder)holder).recipeImageView.setImageBitmap(recipeData.get(i).getRecipeIamge());
                /*Glide.with(ct)
                        .load(recipeData.get(i).getRecipeImageUrl())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(((RecipeAdapter.MyViewHolder)holder).recipeImageView);*/


                ((RecipeAdapter.MyViewHolder)holder).favouriteButton.setOnCheckedChangeListener(null);
                ((RecipeAdapter.MyViewHolder)holder).favouriteButton.setChecked(recipeData.get(i).isfRIndicator());
                ((RecipeAdapter.MyViewHolder)holder).favouriteButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        Log.d("onCheckedChanged", Boolean.toString(isChecked));
                        if(isChecked)
                        {
                            final FavouriteRecipe fR = new FavouriteRecipe(recipeData.get(i).getRecipeId(),recipeData.get(i).getRecipeTitle());
                            fR.setPrimaryKey(Integer.valueOf(recipeData.get(i).getRecipeId()));
                            fRViewModel.insert(fR);
                        }else{
                            FavouriteRecipe fR = new FavouriteRecipe(recipeData.get(i).getRecipeId(),recipeData.get(i).getRecipeTitle());
                            fR.setPrimaryKey(Integer.valueOf(recipeData.get(i).getRecipeId()));
                            fRViewModel.delete(fR);
                        }
                        //notifyItemChanged(i);
                        //modifyData(recipe_title,image_link,recipe_id,recipe_status,fRIndicators);
                    }
                });
            case 1:
            case 2:
            case 3:
        }
    }

    @Override
    public int getItemViewType(int position) {

        return recipeData.get(position).getStatusForDisplay();
    }

    @Override
    public int getItemCount() {
        return recipeData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView recipeImageView;
        AppCompatTextView titleText;
        ToggleButton favouriteButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeImageView = itemView.findViewById(R.id.recipe_image);
            titleText = itemView.findViewById(R.id.recipe_title);
            //titleText.setAutoSizeTextTypeUniformWithConfiguration(8,24,2,1);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ct, RecipeActivity.class);
                    intent.putExtra("id", recipeData.get(getAdapterPosition()).getRecipeId());
                    intent.putExtra("title", recipeData.get(getAdapterPosition()).getRecipeTitle());
                    intent.putExtra("favourite_recipe", favouriteButton.isChecked());
                    ct.startActivity(intent);
                }
            });
            favouriteButton = itemView.findViewById(R.id.favourite_toggle_button);

        }
    }

    private class EndViewHolder extends RecyclerView.ViewHolder{

        public EndViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    private class LoadViewHolder extends RecyclerView.ViewHolder{

        public LoadViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    private class PaddingViewHolder extends RecyclerView.ViewHolder{

        public PaddingViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
