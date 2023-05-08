package com.jasonstudio.cookbook2.view.videos

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.jasonstudio.cookbook2.databinding.FragmentVideoBinding
import com.jasonstudio.cookbook2.databinding.FragmentVideoListBinding
import com.jasonstudio.cookbook2.ext.upsertString
import com.jasonstudio.cookbook2.util.LogUtil

class VideoFragment: Fragment() {
    private var _binding: FragmentVideoBinding? = null
    private val binding: FragmentVideoBinding
        get() = _binding!!
    var onVideoFragmentListener: OnVideoFragmentListener? = null

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.wvVideo.saveState(outState)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVideoBinding.inflate(inflater)
        if (savedInstanceState == null) {
            arguments?.let {
                loadWebpage(it.getString(VIDEO_ID_KEY, ""))
            }
        }
        setupView()
        return binding.root
    }

    private fun loadWebpage(youtubeId: String) {
        binding.wvVideo.loadUrl("https://www.youtube.com/embed/$youtubeId")

//        binding.wvVideo.loadUrl("file:///android_asset/video_player.html")
        binding.wvVideo.settings.apply {
//            loadWithOverviewMode = true
//            useWideViewPort = true
            javaScriptEnabled = true
        }
//        binding.wvVideo.viewTreeObserver.addOnScrollChangedListener(object :
//            ViewTreeObserver.OnScrollChangedListener {
//            override fun onScrollChanged() {
//                if (binding.wvVideo.scrollX < binding.wvVideo.width &&
//                    0 < binding.wvVideo.scrollX) {
//                    binding.wvVideo.requestDisallowInterceptTouchEvent(true)
//                    LogUtil.log(binding.wvVideo.scrollX)
//                } else {
//                    binding.wvVideo.requestDisallowInterceptTouchEvent(false)
//                    LogUtil.log(binding.wvVideo.scrollX)
//                }
//            }
//
//        })
//        binding.wvVideo.setOnTouchListener(object : View.OnTouchListener {
//            override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
//                if (motionEvent.action == MotionEvent.ACTION_DOWN) {
//                    if (binding.wvVideo.scrollX == 0 || binding.wvVideo.scrollX == binding.wvVideo.width) {
//                        binding.wvVideo.requestDisallowInterceptTouchEvent(false)
//                    } else {
//                        binding.wvVideo.requestDisallowInterceptTouchEvent(true)
//                    }
//                    LogUtil.log(binding.wvVideo.scrollX, binding.wvVideo.width, binding.wvVideo.contentHeight)
//                } else if (motionEvent.action == MotionEvent.ACTION_MOVE) {
//
//                }
//                return false
//            }
//        })
    }

    private fun setupView() {
        binding.ivExpandCollapse.setOnClickListener {
            onVideoFragmentListener?.onExpandBtnClicked()
        }
        binding.ivClose.setOnClickListener {
            onVideoFragmentListener?.onCloseBtnClicked()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        val VIDEO_ID_KEY = "VIDEO_ID_KEY"
        fun newInstance(youtubeId: String): VideoFragment {
            val fragment = VideoFragment()
            fragment.upsertString(VIDEO_ID_KEY, youtubeId)
            return fragment
        }
    }
}