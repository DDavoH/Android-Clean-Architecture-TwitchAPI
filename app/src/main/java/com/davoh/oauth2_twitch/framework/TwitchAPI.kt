package com.davoh.oauth2_twitch.framework

import com.davoh.oauth2_twitch.framework.responses.TopGamesResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface TwitchAPI {

    @GET("games/top")
    fun getTopGames():Call<TopGamesResponse>


    companion object {
        private const val BASE_URL = "https://api.twitch.tv/helix/"
        fun create(): TwitchAPI {
            val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TwitchAPI::class.java)
        }
    }
}