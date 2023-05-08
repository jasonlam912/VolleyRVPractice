package com.jasonstudio.cookbook2.view.activity

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.jasonstudio.cookbook2.databinding.ActivityAboutUsBinding
import com.jasonstudio.cookbook2.util.LogUtil
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import com.stripe.android.paymentsheet.addresselement.AddressDetails
import com.stripe.android.paymentsheet.addresselement.AddressLauncher
import com.stripe.android.paymentsheet.addresselement.AddressLauncherResult
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class AboutUsActivity: BaseActivity<ActivityAboutUsBinding>(ActivityAboutUsBinding::inflate) {

    companion object {
        private const val TAG = "AboutUsActivity"
        private const val BACKEND_URL = "http://10.0.2.2:4242"
    }

    private lateinit var paymentIntentClientSecret: String
    private lateinit var paymentSheet: PaymentSheet


    private lateinit var addressLauncher: AddressLauncher

    private var shippingDetails: AddressDetails? = null

    private val addressConfiguration = AddressLauncher.Configuration(
        additionalFields = AddressLauncher.AdditionalFieldsConfiguration(
            phone = AddressLauncher.AdditionalFieldsConfiguration.FieldConfiguration.REQUIRED
    ),
    allowedCountries = setOf("US", "CA", "GB"),
    title = "Shipping Address",
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        bind()
    }
    private fun setupView() {
        title = "About Us"
        binding.cvDonate.setOnClickListener(::onPayClicked)
        paymentSheet = PaymentSheet(this, ::onPaymentSheetResult)
        fetchPaymentIntent()
    }

    private fun fetchPaymentIntent() {
        val url = "$BACKEND_URL/create-payment-intent"

        val shoppingCartContent = """
            {
                "items": [
                    {"id":"xl-tshirt"}
                ]
            }
        """

        val mediaType = MediaType.get("application/json; charset=utf-8")

        val body = RequestBody.create(mediaType, shoppingCartContent)
        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()

        OkHttpClient()
            .newCall(request)
            .enqueue(object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                    showAlert("Failed to load data", "Error: $e")
                }

                override fun onResponse(call: Call, response: Response) {
                    if (!response.isSuccessful) {
                        showAlert("Failed to load page", "Error: $response")
                    } else {
                        val responseData = response.body()?.string()
                        val responseJson = responseData?.let { JSONObject(it) } ?: JSONObject()
                        paymentIntentClientSecret = responseJson.getString("clientSecret")
                        runOnUiThread { binding.cvDonate.isEnabled = true }
                        LogUtil.log(TAG, "Retrieved PaymentIntent")
                    }
                }
            })
    }

    private fun showAlert(title: String, message: String? = null) {
        runOnUiThread {
            val builder = AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
            builder.setPositiveButton("Ok", null)
            builder.create().show()
        }
    }

    private fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(this,  message, Toast.LENGTH_LONG).show()
        }
    }

    private fun onPayClicked(view: View) {
        val configuration = PaymentSheet.Configuration("Example, Inc.")

        // Present Payment Sheet
        paymentSheet.presentWithPaymentIntent(paymentIntentClientSecret, configuration)
    }

//    private fun onAddressClicked(view: View) {
//        addressLauncher.present(
//            publishableKey = publishableKey,
//            configuration = addressConfiguration
//        )
//    }

    private fun onPaymentSheetResult(paymentResult: PaymentSheetResult) {
        when (paymentResult) {
            is PaymentSheetResult.Completed -> {
                showToast("Payment complete!")
            }
            is PaymentSheetResult.Canceled -> {
                LogUtil.log(TAG, "Payment canceled!")
            }
            is PaymentSheetResult.Failed -> {
                showAlert("Payment failed", paymentResult.error.localizedMessage)
            }
        }
    }

    private fun onAddressLauncherResult(result: AddressLauncherResult) {
        // TODO: Handle result and update your UI
        when (result) {
            is AddressLauncherResult.Succeeded -> {
                shippingDetails = result.address
            }
            is AddressLauncherResult.Canceled -> {
                // TODO: Handle cancel
            }
        }
    }

    private fun bind() {

    }

}