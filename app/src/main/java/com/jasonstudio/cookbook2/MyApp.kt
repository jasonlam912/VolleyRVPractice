package com.jasonstudio.cookbook2

import android.app.Application
import android.content.Context
import com.jasonstudio.cookbook2.Network.WebsocketService
import com.jasonstudio.cookbook2.helper.SharedPref
import com.stripe.android.PaymentConfiguration

class MyApp : Application() {


    override fun onCreate() {
        super.onCreate()
        context = this
        PaymentConfiguration.init(
            applicationContext,
            "pk_test_51N565fDsCppcPqAiCKggVn3K9PHz35xCmfNJwn9pXqlaI1SQnfNgNnPiUngXfvVmjNiXrGlh0sQMYBF2LYwlqsxn00gFlajaSs"
        )
        SharedPref.init(applicationContext)
        WebsocketService.getInstance(this)
    }
    companion object {
        lateinit var context: Context
        private set
    }
}