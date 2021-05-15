package com.davoh.oauth2_twitch.data

import com.davoh.oauth2_twitch.domain.AccessToken
import com.davoh.oauth2_twitch.domain.Game
import com.davoh.oauth2_twitch.domain.RefreshToken
import com.davoh.oauth2_twitch.domain.TopGames
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single

interface RemoteTopGamesDataSource{
    fun getTopGames( authHeader: String,
                     clientId: String,
                     after: String,
                     before:String,
                     first:Int): Single<TopGames>
}

interface RemoteOAuth2TwitchDataSource{
    fun getToken(clientId: String,
                 clientSecret: String,
                 code: String,
                 grantType: String,
                 redirectUri: String): Single<AccessToken>

    fun revokeToken(clientId:String,
    token:String): Completable

    fun refreshToken(grantType: String,
                     refreshToken:String,
                     clientId:String,
                     clientSecret:String):Single<RefreshToken>
}

interface LocalGameDataSource{
    fun getAllFavoriteGames(): Flowable<List<Game>>
    fun getFavoriteGameStatus(gameId:Int) : Maybe<Boolean>
    fun updateGameStatus(game:Game) : Maybe<Boolean>
}