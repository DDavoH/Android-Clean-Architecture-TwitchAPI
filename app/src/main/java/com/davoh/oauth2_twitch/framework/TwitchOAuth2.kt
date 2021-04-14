package com.davoh.oauth2_twitch.framework

import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET

interface TwitchOAuth2 {
    @GET("oauth2/authorize")
    fun getToken(@Body storeToken: StoreToken): Call<ResponseBody>

    companion object{
        private const val BASE_URL = "https://id.twitch.tv/"
        fun create(): TwitchOAuth2{
            val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TwitchOAuth2::class.java)
        }
    }
}