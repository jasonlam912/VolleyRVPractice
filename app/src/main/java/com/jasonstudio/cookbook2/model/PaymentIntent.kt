package com.jasonstudio.cookbook2.model

import com.squareup.moshi.Json

data class PaymentIntent (
    val id: String,

    @field:Json(name = "object")
    val paymentIntentObject: String,

    val amount: Long,

    @field:Json(name = "amount_capturable")
    val amountCapturable: Long,

    @field:Json(name = "amount_details")
    val amountDetails: AmountDetails,

    @field:Json(name = "amount_received")
    val amountReceived: Long,

    @field:Json(name = "capture_method")
    val captureMethod: String,

    @field:Json(name = "client_secret")
    val clientSecret: String,

    @field:Json(name = "confirmation_method")
    val confirmationMethod: String,

    val created: Long,
    val currency: String,
    val description: String,
    val livemode: Boolean,
    val metadata: Metadata,

    @field:Json(name = "automatic_payment_methods")
    val automaticPaymentMethods: AutomaticPaymentMethods,

    @field:Json(name = "payment_method_options")
    val paymentMethodOptions: PaymentMethodOptions,

    @field:Json(name = "payment_method_types")
    val paymentMethodTypes: List<String>,

    val status: String
)

data class AmountDetails (
    val tip: Metadata
)

class Metadata

data class AutomaticPaymentMethods (
    val enabled: Boolean
)

data class PaymentMethodOptions (
    val card: Card
)

data class Card (
    @field:Json(name = "request_three_d_secure")
    val requestThreeDSecure: String
)