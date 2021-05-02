package com.davoh.oauth2_twitch.data

import com.davoh.oauth2_twitch.domain.AccessToken
import com.davoh.oauth2_twitch.domain.TopGames
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class TopGamesRepository(
    private val remoteTopGamesDataSource: RemoteTopGamesDataSource
) {
    fun getTopGames(authHeader: String,
                     clientId: String,
                     after: String,
                     before:String,
                    first:Int): Single<TopGames> =
        remoteTopGamesDataSource.getTopGames(authHeader,clientId, after,before,first)
}

class OAuth2TwitchRepository(
    private val remoteOAuth2TwitchDataSource: RemoteOAuth2TwitchDataSource
){
    fun getToken(clientId: String,
                 clientSecret: String,
                 code: String,
                 grantType: String,
                 redirectUri: String): Single<AccessToken> =
        remoteOAuth2TwitchDataSource.getToken(clientId, clientSecret, code, grantType, redirectUri)

    fun revokeToken(clientId:String,
    token:String): Completable =
        remoteOAuth2TwitchDataSource.revokeToken(clientId, token)
}