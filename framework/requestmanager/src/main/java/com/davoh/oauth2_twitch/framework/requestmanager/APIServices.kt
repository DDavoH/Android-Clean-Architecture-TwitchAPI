package com.davoh.oauth2_twitch.framework.requestmanager

import com.davoh.oauth2_twitch.framework.requestmanager.APIConstants.ENDPOINT_GAMES
import com.davoh.oauth2_twitch.framework.requestmanager.APIConstants.ENDPOINT_OAUTH2
import com.davoh.oauth2_twitch.framework.requestmanager.APIConstants.ENDPOINT_OAUTH2_REVOKE
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import okhttp3.ResponseBody
import retrofit2.http.*

interface TwitchOAuth2Service {
    @POST(ENDPOINT_OAUTH2)
    @FormUrlEncoded
    fun getToken(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("code") code: String,
        @Field("grant_type") grantType: String,
        @Field("redirect_uri") redirectUri: String
    ): Single<AccessTokenResponse>

    @POST(ENDPOINT_OAUTH2_REVOKE)
    @FormUrlEncoded
    fun revokeToken(
        @Field("client_id") clientId: String,
        @Field("token") token: String
    ): Completable

    @POST(ENDPOINT_OAUTH2)
    @FormUrlEncoded
    fun refreshToken(
        @Field("grant_type") grantType: String,
        @Field("refresh_token") refreshToken:String,
        @Field("client_id") clientId:String,
        @Field("client_secret") clientSecret:String
    ): Single<RefreshTokenResponse>
}

interface TwitchTopGamesService {
    @GET(ENDPOINT_GAMES)
    fun getTopGames(
        @Header("Authorization") authHeader: String,
        @Header("Client-Id") clientId: String,
        @Query("after") after: String = "",
        @Query("before") before: String = "",
        @Query("first") first: Int
    ): Single<TopGamesResponse>
}