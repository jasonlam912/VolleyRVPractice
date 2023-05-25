package com.jasonstudio.cookbook2.view.videos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.jasonstudio.cookbook2.EndViewHolder
import com.jasonstudio.cookbook2.LoadViewHolder
import com.jasonstudio.cookbook2.R
import com.jasonstudio.cookbook2.databinding.RowEndBinding
import com.jasonstudio.cookbook2.databinding.RowLoadingBinding
import com.jasonstudio.cookbook2.databinding.RowVideoBinding
import com.jasonstudio.cookbook2.model.Video

class VideosAdapter(val listener: OnVideoClickListener):
    RecyclerView.Adapter<ViewHolder>() {

    private val data = mutableListOf<Video>()
    private var isLastData = false

    override fun getItemViewType(position: Int): Int {
        return if (position < data.size) {
            0
        } else {
            if (isLastData) {
                2
            } else {
                1
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        when (viewType) {
            0 -> {
                val binding = RowVideoBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return VideoViewHolder(binding)
            }
            1 -> {
                val binding = RowLoadingBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return LoadViewHolder(binding.root, R.color.black)
            }
            2 -> {
                val binding = RowEndBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return EndViewHolder(binding.root, R.color.black)
            }
            else -> return EndViewHolder(parent)
        }

    }
    override fun getItemCount(): Int {
        return data.size + 1
    }
    fun addData(newData: List<Video>, from: Int, count: Int, isLastData: Boolean) {
        this.data.addAll(newData)
        this.isLastData = isLastData
        notifyItemRangeChanged(0,data.size)
//        notifyItemRangeChanged(from, count)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder.itemViewType) {
            0 -> {
                (holder as VideoViewHolder).let { holder ->
                    holder.binding.model = data[position]
                    holder.binding.apply {
                        root.setOnClickListener {
                            listener.onVideoClicked(data[position])
                        }
                    }
                }
            }
        }

    }
}

class VideoViewHolder(val binding: RowVideoBinding): ViewHolder(binding.root)