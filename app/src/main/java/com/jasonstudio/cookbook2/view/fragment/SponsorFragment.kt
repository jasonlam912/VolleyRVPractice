package com.jasonstudio.cookbook2.view.fragment
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.jasonstudio.cookbook2.BaseFragment
import com.jasonstudio.cookbook2.databinding.FragmentSponsorBinding
import com.jasonstudio.cookbook2.viewmodel.SponsorViewModel

class SponsorFragment: BaseFragment<FragmentSponsorBinding>(FragmentSponsorBinding::inflate) {

    private val model: SponsorViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        bind()
    }

    private fun setupView() {
        
    }
    
    private fun bind() {
        
    }

    companion object {
        fun getInstance(): SponsorFragment {
            return SponsorFragment()
        }
    }
}