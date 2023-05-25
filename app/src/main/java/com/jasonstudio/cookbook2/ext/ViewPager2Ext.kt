package com.jasonstudio.cookbook2.ext

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.jasonstudio.cookbook2.util.LogUtil
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


fun ViewPager2.addAutoScroll(ms: Long) {
    var page = 0
    val numPages = 3
    var job: Job? = null
    fun startJob() {
        job = findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
            delay(ms)
            page = (++page) % numPages
            this@addAutoScroll.setCurrentItem(page, true)
            startJob()
        }
    }
    val callback = object : ViewPager2.OnPageChangeCallback() {
        var isSuspended = false
            set(value) {
                if (field == value) {
                    return
                }
                field = value
                if (value) {
                    job?.cancel()
                } else {
                    startJob()
                }
            }
        init {
            startJob()
        }

        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            isSuspended = false
            LogUtil.log(position)
        }

        override fun onPageScrollStateChanged(@ViewPager2.ScrollState state: Int) {
            super.onPageScrollStateChanged(state)
            when (state) {
                ViewPager2.SCROLL_STATE_SETTLING -> isSuspended = true
                ViewPager2.SCROLL_STATE_IDLE -> isSuspended = false
                ViewPager2.SCROLL_STATE_DRAGGING -> isSuspended = true
            }
            LogUtil.log(state)
        }
    }
    registerOnPageChangeCallback(callback)
}

fun ViewPager2.addAutoScrollHandler(autoScrollMs: Long) {
    registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
        var page = 0
        val numPages = 3
        var isSuspended = false
            set(value) {
                if (field == value) {
                    return
                }
                field = value
                if (value) {
                    fragHandler.removeCallbacks(update)
                } else {
                    fragHandler.postDelayed(update, autoScrollMs)
                }
            }

        val update = object : Runnable {
            override fun run() {
                page = (++page) % numPages
                this@addAutoScrollHandler.setCurrentItem(page, true)
                fragHandler.postDelayed(this, autoScrollMs)
            }
        }
        val fragHandler = Handler(Looper.getMainLooper())

        init {
            fragHandler.postDelayed(update, autoScrollMs)
        }

        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            isSuspended = false
            LogUtil.log(position)
        }
        override fun onPageScrollStateChanged(@ViewPager2.ScrollState state: Int) {
            super.onPageScrollStateChanged(state)
            when (state) {
                ViewPager2.SCROLL_STATE_SETTLING -> isSuspended = true
                ViewPager2.SCROLL_STATE_IDLE -> isSuspended = false
                ViewPager2.SCROLL_STATE_DRAGGING -> isSuspended = true
            }
            LogUtil.log(state)
        }
    })
}