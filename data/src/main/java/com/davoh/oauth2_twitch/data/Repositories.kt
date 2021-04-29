package com.davoh.oauth2_twitch.data

import com.davoh.oauth2_twitch.domain.AccessToken
import com.davoh.oauth2_twitch.domain.Game
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class TopGamesRepository(
    private val remoteTopGamesDataSource: RemoteTopGamesDataSource
) {
    fun getTopGames( authHeader: String,
                     clientId: String,
                     after: String): Single<List<Game>> =
        remoteTopGamesDataSource.getTopGames(authHeader,clientId, after)
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
}