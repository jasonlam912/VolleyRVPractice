package com.jasonstudio.cookbook2.view.activity


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.jasonstudio.cookbook2.Network.WebsocketService
import com.jasonstudio.cookbook2.util.LogUtil
import org.java_websocket.enums.ReadyState


open class WebsocketActivity: MessagingActivity() {

    var isInFront = false
        private set
    private var service = WebsocketService.getInstance()
    private var mMessageReceiver: BroadcastReceiver? = null
    private var snackBar: Snackbar? = null
        set(value) {
            value?.let {
                value.show()
                field = it
            } ?: run {
                field?.dismiss()
                field = value
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isInFront = true
        sendData(service.readyState)
        mMessageReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val actionStr = intent.action ?: return
                val action = ReadyState.valueOf(actionStr)
                if (!this@WebsocketActivity.isInFront) {
                    return
                }
                sendData(action)
            }
        }
        mMessageReceiver?.let {
            LocalBroadcastManager.getInstance(this).registerReceiver(it, IntentFilter(ReadyState.CLOSED.toString()))
            LocalBroadcastManager.getInstance(this).registerReceiver(it, IntentFilter(ReadyState.OPEN.toString()))
            LocalBroadcastManager.getInstance(this).registerReceiver(it, IntentFilter(ReadyState.NOT_YET_CONNECTED.toString()))
            LocalBroadcastManager.getInstance(this).registerReceiver(it, IntentFilter(ReadyState.CLOSING.toString()))
        }
    }

    private fun sendData(readyState: ReadyState) {
        LogUtil.log(readyState)
        when (readyState) {
            ReadyState.OPEN -> {
                snackBar = null
            }
            ReadyState.NOT_YET_CONNECTED,
            ReadyState.CLOSING,
            ReadyState.CLOSED, -> {
                if (snackBar == null) {
                    snackBar = Snackbar.make(
                        this@WebsocketActivity,
                        window.decorView.findViewById(android.R.id.content),
                        "disconnected",
                        BaseTransientBottomBar.LENGTH_INDEFINITE
                    )
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        isInFront = false
    }
}