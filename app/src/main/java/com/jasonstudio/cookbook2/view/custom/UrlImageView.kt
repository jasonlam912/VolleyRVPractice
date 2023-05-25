package com.jasonstudio.cookbook2.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.jasonstudio.cookbook2.databinding.LayoutUrlImageViewBinding
import com.jasonstudio.cookbook2.util.LogUtil

class UrlImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private var _binding: LayoutUrlImageViewBinding? = null
    val binding: LayoutUrlImageViewBinding
    get() {
        if (_binding == null) {
            initView()
        }
        return _binding!!
    }

    private fun initView() {
        _binding = LayoutUrlImageViewBinding.inflate(
            LayoutInflater.from(context),
            this
        )
    }
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        LogUtil.log("here")
    }
}