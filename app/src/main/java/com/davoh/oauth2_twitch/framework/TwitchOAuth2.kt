package com.davoh.oauth2_twitch.framework

import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface TwitchOAuth2 {

    @POST("oauth2/token")
    @FormUrlEncoded
    fun getToken(@Field("client_id") clientId: String,
                 @Field("client_secret") clientSecret: String,
                 @Field("code") code: String,
                 @Field("grant_type") grantType: String,
                 @Field("redirect_uri") redirectUri: String): Call<AccessToken>

    companion object {
        private const val BASE_URL = "https://id.twitch.tv/"
        fun create(): TwitchOAuth2 {
            val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

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