package com.jasonstudio.cookbook2.databinding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.jasonstudio.cookbook2.view.custom.UrlImageView

@BindingAdapter("image_url")
fun UrlImageView.loadImage(url: String?) {
    url?.let {
        Glide.with(this.context).load(it).into(binding.iv)
    }
}