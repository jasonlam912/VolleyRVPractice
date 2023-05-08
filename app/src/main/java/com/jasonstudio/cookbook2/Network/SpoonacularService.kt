package com.jasonstudio.cookbook2.Network

import androidx.lifecycle.LiveData
import com.jasonstudio.cookbook2.helper.SharedPref
import com.jasonstudio.cookbook2.model.*
import com.jasonstudio.cookbook2.util.LogUtil
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit


interface SpoonacularService {

    @GET("/food/videos/search")
    suspend fun getVideos(
        @Query("query") query: String? = null,
        @Query("type") type: String? = null,
        @Query("cuisine") cuisine: String? = null,
        @Query("diet") diet: String? = null,
        @Query("includeIngredients") includeIngredients: String? = null,
        @Query("excludeIngredients") excludeIngredients: String? = null,
        @Query("minLength") minLength: Int? = null,
        @Query("maxLength") maxLength: Int? = null,
        @Query("offset") offset: Int? = null,
        @Query("number") number: Int? = null
    ): Response<VideosResponse>

    @GET("/recipes/random")
    suspend fun getRandomRecipes(
        @Query("number") number: Int = 10
    ): Response<RecipeResponse>

    @GET("/recipes/complexSearch")
    suspend fun searchRecipes(
        @Query("query") query: String,
        @Query("number") number: Int = 10,
        @Query("offset") offset: Int? = null,
    ): Response<SearchRecipeResponse>

    @GET("/recipes/{id}/information")
    suspend fun getIngredients(
        @Path("id") id: String,
        @Query("includeNutrition") includeNutrition: Boolean = false,
    ): Response<IngredientsResponse>

    @GET("/recipes/{id}/analyzedInstructions")
    suspend fun getInstructions(
        @Path("id") id: String,
    ): Response<MutableList<Instruction>>

    @GET("recipes/{id}/nutritionWidget.json")
    suspend fun getNutritions(
        @Path("id") id: String
    ): Response<NutritionResponse>

    companion object {
        private var service: SpoonacularService? = null
        val apiKeys = listOf(
            "22490cb12d2d45c7aafe40f28b4aa804",
            "17d59d79ec404c85838d4731da66410a",
            "e381209459fb4d98aa4af55fb08363bd",
            "c93c4d8ad0494097a210bb69259e130f",
            "ac47e36bfcac42559c879f74ad926935",
        )
        val apiKey: String
        get() = apiKeys[SharedPref.getApiKeyIndex()]
        fun getInstance(): SpoonacularService {
            service?.let {
                return it
            } ?: run {
                service = getRetrofit().create(SpoonacularService::class.java)
                return service!!
            }
        }
        private fun getRetrofit(): Retrofit = Retrofit.Builder()
            .client(getOkHttpClient())
            .baseUrl("https://api.spoonacular.com")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        private fun getOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100, TimeUnit.SECONDS)
            .addInterceptor(getInterceptor())
            .addInterceptor(getReloadInterceptor())
            .build()

        private fun getInterceptor() = object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
                val original: Request = chain.request()
                val originalHttpUrl: HttpUrl = original.url()
                val url = originalHttpUrl.newBuilder()
                    .addQueryParameter("apiKey", apiKey)
                    .build()
                val requestBuilder: Request.Builder = original.newBuilder()
                    .url(url)
                val request: Request = requestBuilder.build()
                return chain.proceed(request)
            }
        }

        private fun getReloadInterceptor() = Interceptor {
            val request = it.request()
            LogUtil.log(SharedPref.getApiKeyIndex(), request.url().url())
            var response: okhttp3.Response = it.proceed(request)
            while (response.code() == 402) {
                SharedPref.addApiKeyIndex()
                val newUrl = request
                    .url()
                    .newBuilder()
                    .setQueryParameter("apiKey", apiKey)
                    .build()
                val newRequest = request.newBuilder()
                    .url(newUrl)
                    .build()
                response.close()
                response = it.proceed(newRequest)
            }
            response
        }
    }
}