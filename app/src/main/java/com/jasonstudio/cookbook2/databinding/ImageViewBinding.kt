package com.jasonstudio.cookbook2.databinding

import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.jasonstudio.cookbook2.ext.addInterceptor
import com.jasonstudio.cookbook2.view.custom.UrlImageView


@BindingAdapter("image_url")
fun UrlImageView.loadImage(url: String?) {
    url?.let {
        Glide.with(this.context)
            .load(it)
            .addInterceptor()
            .timeout(20000)
            .into(binding.iv)
    }
}
