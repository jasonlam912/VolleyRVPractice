package com.jasonstudio.cookbook2.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.jasonstudio.cookbook2.databinding.RowBannerBinding

class BannerAdapter: RecyclerView.Adapter<BannerViewHolder>() {
    private val data = listOf(
        "https://contentstatic.techgig.com/thumb/msid-77087595,width-2000,resizemode-4/Guide-How-to-build-career-as-a-programmer-without-college-degree.jpg?336360",
        "https://kinsta.com/wp-content/uploads/2022/03/what-is-express-js-1.jpg",
        "https://static.vecteezy.com/system/resources/previews/015/642/935/original/website-programming-and-coding-web-development-and-coding-3d-gradient-isometric-illustrations-suitable-for-ui-ux-web-mobile-banner-and-infographic-vector.jpg"
    )
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val binding = RowBannerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BannerViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        holder.binding.url = data[position]
    }
}

class BannerViewHolder(val binding: RowBannerBinding): ViewHolder(binding.root)