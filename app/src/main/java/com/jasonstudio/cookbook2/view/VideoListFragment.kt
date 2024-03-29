package com.jasonstudio.cookbook2.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.jasonstudio.cookbook2.BaseFragment
import com.jasonstudio.cookbook2.databinding.FragmentVideoListBinding
import com.jasonstudio.cookbook2.model.Video
import com.jasonstudio.cookbook2.view.videos.OnVideoClickListener
import com.jasonstudio.cookbook2.view.videos.VideosAdapter
import com.jasonstudio.cookbook2.viewmodel.VideosModel

class VideoListFragment: BaseFragment<FragmentVideoListBinding>(FragmentVideoListBinding::inflate), OnVideoClickListener {
    private val model: VideosModel by viewModels()
    private lateinit var adapter: VideosAdapter
    private val manager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    var onVideoClickListener: OnVideoClickListener? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        bind()
    }
    private fun bind() {
        model.videos.observe(viewLifecycleOwner) {
            adapter.addData(it, model.offset, model.batchSize, model.isLastData)
        }
        model.getVideos(arguments?.getString("title") ?: "")
    }

    private fun setupView() {
        binding.rvVideos.apply {
            layoutManager = manager
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        }
        adapter = VideosAdapter(this)
        binding.rvVideos.adapter = adapter
        binding.rvVideos.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lastVisiblePos = manager.findLastVisibleItemPosition()
                model.getMoreVideos(lastVisiblePos)
            }
        })
    }

    override fun onVideoClicked(video: Video) {
        onVideoClickListener?.onVideoClicked(video)
    }

    companion object {
        fun getInstance(): VideoListFragment {
            return VideoListFragment()
        }
    }
}