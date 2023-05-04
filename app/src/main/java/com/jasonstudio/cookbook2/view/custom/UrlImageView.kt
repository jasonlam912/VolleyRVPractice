package com.jasonstudio.cookbook2.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.getSystemService
import com.jasonstudio.cookbook2.R
import com.jasonstudio.cookbook2.databinding.LayoutUrlImageViewBinding

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
}