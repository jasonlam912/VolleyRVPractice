package com.jasonstudio.cookbook2.view.activity

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.jasonstudio.cookbook2.Network.CookBookService
import com.jasonstudio.cookbook2.databinding.ActivityAboutUsBinding
import com.jasonstudio.cookbook2.ext.addAutoScroll
import com.jasonstudio.cookbook2.model.PaymentItemRequest
import com.jasonstudio.cookbook2.util.LogUtil
import com.jasonstudio.cookbook2.view.adapter.BannerAdapter
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import com.stripe.android.paymentsheet.addresselement.AddressDetails
import com.stripe.android.paymentsheet.addresselement.AddressLauncher
import com.stripe.android.paymentsheet.addresselement.AddressLauncherResult
import kotlinx.coroutines.launch


class AboutUsActivity: BaseActivity<ActivityAboutUsBinding>(ActivityAboutUsBinding::inflate) {

    companion object {
        private const val TAG = "AboutUsActivity"
    }

    private lateinit var paymentIntentClientSecret: String
    private lateinit var paymentSheet: PaymentSheet


//    private lateinit var addressLauncher: AddressLauncher
//    private var shippingDetails: AddressDetails? = null
//    private val addressConfiguration = AddressLauncher.Configuration(
//        additionalFields = AddressLauncher.AdditionalFieldsConfiguration(
//            phone = AddressLauncher.AdditionalFieldsConfiguration.FieldConfiguration.REQUIRED
//    ),
//    allowedCountries = setOf("US", "CA", "GB"),
//    title = "Shipping Address",
//    )




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        bind()
    }
    private fun setupView() {
        title = "About Us"
        binding.cvDonate.setOnClickListener {
            onPayClicked()
        }
        paymentSheet = PaymentSheet(this, ::onPaymentSheetResult)
        fetchPaymentIntent()
        binding.vpBanner.apply {
            adapter = BannerAdapter()
//            adapter = BannerStateAdapter(this@AboutUsActivity)
            addAutoScroll(5000)
        }
        binding.btnBiometric.setOnClickListener {
            startActivity(Intent(this@AboutUsActivity, BiometricActivity::class.java))
        }
    }

    private fun fetchPaymentIntent() {
        lifecycleScope.launch {
            val res = CookBookService.getInstance().getPaymentIntent(
                PaymentItemRequest(
                    items = "new items"
                )
            )
            if (!res.isSuccessful) {
                showAlert("Failed to load page", "Error: $res")
            } else {
                res.body()?.let {
                    paymentIntentClientSecret = it.clientSecret
                    binding.cvDonate.isEnabled = true
                    LogUtil.log(TAG, "Retrieved PaymentIntent")
                }
            }
        }
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

    private fun onPayClicked() {
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

//    private fun onAddressLauncherResult(result: AddressLauncherResult) {
//        // TODO: Handle result and update your UI
//        when (result) {
//            is AddressLauncherResult.Succeeded -> {
//                shippingDetails = result.address
//            }
//            is AddressLauncherResult.Canceled -> {
//                // TODO: Handle cancel
//            }
//        }
//    }

    private fun bind() {

    }

}