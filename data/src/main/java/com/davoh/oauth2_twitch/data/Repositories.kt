package com.davoh.oauth2_twitch.data

import com.davoh.oauth2_twitch.domain.AccessToken
import com.davoh.oauth2_twitch.domain.Game
import com.davoh.oauth2_twitch.domain.RefreshToken
import com.davoh.oauth2_twitch.domain.TopGames
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single

class TopGamesRepository(
    private val remoteTopGamesDataSource: RemoteTopGamesDataSource,
    private val localGameDataSource: LocalGameDataSource
) {
    fun getTopGames(authHeader: String,
                     clientId: String,
                     after: String,
                     before:String,
                    first:Int): Single<TopGames> =
        remoteTopGamesDataSource.getTopGames(authHeader,clientId, after,before,first)

    fun getAllFavoriteGames(): Flowable<List<Game>> =
        localGameDataSource.getAllFavoriteGames()

    fun getFavoriteGameStatus(gameId: Int): Maybe<Boolean> =
        localGameDataSource.getFavoriteGameStatus(gameId)

    fun updateFavoriteGameStatus(game:Game): Maybe<Boolean> =
        localGameDataSource.updateGameStatus(game)

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

    fun refreshToken(grantType: String,
                     refreshToken:String,
                     clientId:String,
                     clientSecret:String):Single<RefreshToken> =
        remoteOAuth2TwitchDataSource.refreshToken(grantType, refreshToken, clientId, clientSecret)
}