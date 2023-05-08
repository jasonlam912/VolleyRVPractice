package com.jasonstudio.cookbook2.viewmodel

import androidx.lifecycle.ViewModel
import com.jasonstudio.cookbook2.Network.SpoonacularService

class SponsorViewModel: ViewModel() {
    private val service = SpoonacularService.getInstance()

    init {

    }
}