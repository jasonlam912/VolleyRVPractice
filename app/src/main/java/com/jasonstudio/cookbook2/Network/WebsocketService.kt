package com.jasonstudio.cookbook2.Network

import android.content.Context
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.jasonstudio.cookbook2.BuildConfig
import com.jasonstudio.cookbook2.ext.BuildConfigObject
import com.jasonstudio.cookbook2.util.LogUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.java_websocket.client.WebSocketClient
import org.java_websocket.enums.ReadyState
import org.java_websocket.handshake.ServerHandshake
import java.net.URI

class WebsocketService private constructor(
    url: URI,
    val context: Context
): WebSocketClient(url) {

    private val viewModels = mutableListOf<WebSocketViewModel>()
    init {
        connect()
    }

    fun addViewModel(model: WebSocketViewModel) {
        viewModels.add(model)
    }

    fun removeViewModel(model: WebSocketViewModel) {
        viewModels.remove(model)
    }

    override fun onOpen(handshakedata: ServerHandshake?) {
        LogUtil.log(handshakedata)
        val it = Intent(ReadyState.OPEN.toString())
        LocalBroadcastManager.getInstance(context).sendBroadcast(it)
    }

    override fun onMessage(message: String?) {
        LogUtil.log(message)
    }

    override fun onClose(code: Int, reason: String?, remote: Boolean) {
        LogUtil.log(code, reason, remote)
        GlobalScope.launch {
            delay(2000)
            if (readyState == ReadyState.CLOSING || readyState == ReadyState.CLOSED) {
                reconnect()
            }
        }
        val it = Intent(ReadyState.CLOSED.toString())
        LocalBroadcastManager.getInstance(context).sendBroadcast(it)
    }

    override fun onError(ex: Exception?) {
        LogUtil.log(ex)
    }

    companion object {
        private val url = "ws${BuildConfigObject.getS()}://${BuildConfig.URL}/echo"
        private var instance: WebsocketService? = null
        fun getInstance(context: Context): WebsocketService {
            instance?.let {
                return it
            } ?: kotlin.run {
                instance = WebsocketService(
                    URI.create(url),
                    context
                )
                return instance!!
            }
        }
        fun getInstance(): WebsocketService {
            return instance!!
        }
    }
}
