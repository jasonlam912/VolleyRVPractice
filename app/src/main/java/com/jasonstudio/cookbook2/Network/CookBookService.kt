package com.jasonstudio.cookbook2.Network

import com.jasonstudio.cookbook2.BuildConfig
import com.jasonstudio.cookbook2.ext.BuildConfigObject
import com.jasonstudio.cookbook2.model.PaymentIntent
import com.jasonstudio.cookbook2.model.PaymentItemRequest
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.util.concurrent.TimeUnit


interface CookBookService {

    @POST("/create-payment-intent")
    suspend fun getPaymentIntent(@Body request: PaymentItemRequest): Response<PaymentIntent>

    companion object {
        val BACKEND_URL = "http${BuildConfigObject.getS()}://${BuildConfig.URL}"
        private var service: CookBookService? = null
        fun getInstance(): CookBookService {
            service?.let {
                return it
            } ?: run {
                service = getRetrofit(
                    BACKEND_URL
                ).create(CookBookService::class.java)
                return service!!
            }
        }
        fun getRetrofit(url: String): Retrofit = Retrofit.Builder()
            .client(getCookBookOkHttpClient())
            .baseUrl(url)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        fun getCookBookOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(InterceptorFactory.getTrafficStatsInterceptor())
            .connectTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100, TimeUnit.SECONDS)
            .build()
    }
}