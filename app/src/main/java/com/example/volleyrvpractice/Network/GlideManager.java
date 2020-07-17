package com.example.volleyrvpractice.Network;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.volleyrvpractice.Recipe.RecipeModel;

import java.util.List;

public class GlideManager {
    public static CustomTarget<Bitmap> loadImage(Context ct, String url, final int i, final GlideCallbackListener listener) {
        return Glide.with(ct).asBitmap().load(url).diskCacheStrategy(DiskCacheStrategy.NONE).into(new CustomTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                //Log.d("onResourceReady",resource.toString();
                listener.getBitmap(resource, i);
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }
        });
    }
}
