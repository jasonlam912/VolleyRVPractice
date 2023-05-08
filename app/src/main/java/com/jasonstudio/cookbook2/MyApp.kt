package com.jasonstudio.cookbook2

import android.app.Application
import com.stripe.android.PaymentConfiguration

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        PaymentConfiguration.init(
            applicationContext,
            "pk_test_51N565fDsCppcPqAiCKggVn3K9PHz35xCmfNJwn9pXqlaI1SQnfNgNnPiUngXfvVmjNiXrGlh0sQMYBF2LYwlqsxn00gFlajaSs"
        )
    }
}