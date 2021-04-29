package com.davoh.oauth2_twitch.framework.requestmanager

import com.davoh.oauth2_twitch.framework.requestmanager.APIConstants.ENDPOINT_GAMES
import com.davoh.oauth2_twitch.framework.requestmanager.APIConstants.ENDPOINT_OAUTH2
import io.reactivex.rxjava3.core.Single
import retrofit2.http.*

interface TwitchOAuth2Service{
    @POST(ENDPOINT_OAUTH2)
    @FormUrlEncoded
    fun getToken(@Field("client_id") clientId: String,
                 @Field("client_secret") clientSecret: String,
                 @Field("code") code: String,
                 @Field("grant_type") grantType: String,
                 @Field("redirect_uri") redirectUri: String): Single<AccessTokenResponse>
}

interface TwitchTopGamesService{
    @GET(ENDPOINT_GAMES)
    fun getTopGames(
        @Header("Authorization") authHeader: String,
        @Header("Client-Id") clientId: String,
        @Header("after") after:String = ""
    ): Single<TopGamesResponse>
}