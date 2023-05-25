package com.jasonstudio.cookbook2.Network

import android.net.TrafficStats
import com.jasonstudio.cookbook2.helper.SharedPref
import com.jasonstudio.cookbook2.util.LogUtil
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request


object InterceptorFactory {

    fun getInterceptor() = Interceptor { chain ->
        val original: Request = chain.request()
        val originalHttpUrl: HttpUrl = original.url
        val url = originalHttpUrl.newBuilder()
            .addQueryParameter("apiKey", SpoonacularService.apiKey)
            .build()
        val requestBuilder: Request.Builder = original.newBuilder()
            .url(url)
        val request: Request = requestBuilder.build()
        chain.proceed(request)
    }

    fun getTrafficStatsInterceptor() = Interceptor {
        TrafficStats.setThreadStatsTag(Thread.currentThread().id.toInt())
        val request = it.request()
        LogUtil.log(request.url)
        it.proceed(request)
    }

    fun getReloadInterceptor() = Interceptor {
        val request = it.request()
        LogUtil.log(SharedPref.getApiKeyIndex(), request.url)
        var response: okhttp3.Response = it.proceed(request)
        while (response.code == 402) {
            SharedPref.addApiKeyIndex()
            val newUrl = request
                .url
                .newBuilder()
                .setQueryParameter("apiKey", SpoonacularService.apiKey)
                .build()
            val newRequest = request.newBuilder()
                .url(newUrl)
                .build()
            response.close()
            response = it.proceed(newRequest)
        }
        response
    }

    fun getSocketInterceptor() = Interceptor {
        TrafficStats.setThreadStatsTag(Thread.currentThread().id.toInt())
        it.proceed(it.request())
    }
}