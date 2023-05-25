package com.jasonstudio.cookbook2.Network

import androidx.lifecycle.ViewModel
import com.jasonstudio.cookbook2.model.WSResponse

abstract class WebSocketViewModel: ViewModel() {
    private val service = WebsocketService.getInstance()
    init {
        service.addViewModel(this)
    }

    override fun onCleared() {
        super.onCleared()
        service.removeViewModel(this)
    }
}

interface WebSocketListener<T: WSResponse> {
    fun onReceivedMessage(message: T)
}