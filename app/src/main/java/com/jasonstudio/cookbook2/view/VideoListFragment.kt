package com.jasonstudio.cookbook2.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnScrollChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jasonstudio.cookbook2.databinding.FragmentVideoListBinding
import com.jasonstudio.cookbook2.util.LogUtil
import com.jasonstudio.cookbook2.view.videos.VideosAdapter
import com.jasonstudio.cookbook2.view.videos.VideosModel

class VideoListFragment: Fragment() {
    private var _binding: FragmentVideoListBinding? = null
    private val binding: FragmentVideoListBinding
    get() = _binding!!
    private val model: VideosModel by viewModels()
    private lateinit var adapter: VideosAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVideoListBinding.inflate(inflater)
        setupView()
        bind()
        return binding.root
    }

    private fun bind() {
        model.videos.observe(viewLifecycleOwner) {
            adapter.addData(it, model.offset, model.batchSize)
        }
        model.getVideos(arguments?.getString("title") ?: "")
    }

    private fun setupView() {
        binding.rvVideos.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            
        }
        adapter = VideosAdapter()
        binding.rvVideos.adapter = adapter
    }
}