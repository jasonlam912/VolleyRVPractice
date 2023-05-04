package com.jasonstudio.cookbook2.view.videos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.jasonstudio.cookbook2.R
import com.jasonstudio.cookbook2.databinding.RowVideoBinding
import com.jasonstudio.cookbook2.model.Video

class VideosAdapter: RecyclerView.Adapter<VideoViewHolder>() {

    private val data = mutableListOf<Video>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val binding = RowVideoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VideoViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun addData(newData: List<Video>, from: Int, count: Int) {
        this.data.addAll(newData)
        notifyItemRangeChanged(from, count)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.binding.model = data[position]
    }
}

class VideoViewHolder(val binding: RowVideoBinding): ViewHolder(binding.root)