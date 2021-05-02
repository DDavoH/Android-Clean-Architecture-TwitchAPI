package com.davoh.oauth2_twitch.usecases

import com.davoh.oauth2_twitch.data.OAuth2TwitchRepository
import com.davoh.oauth2_twitch.data.TopGamesRepository
import com.davoh.oauth2_twitch.domain.AccessToken
import com.davoh.oauth2_twitch.domain.Game
import com.davoh.oauth2_twitch.domain.TopGames
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GetTopGamesUseCase (
    private val topGamesRepository: TopGamesRepository
){
    fun invoke(authHeader: String,
               clientId: String,
               after: String,
               before:String,
               first:Int): Single<TopGames> =
        topGamesRepository.getTopGames(authHeader,clientId, after,before,first)
}

class GetAccessTokenUseCase(
    private val oAuth2TwitchRepository: OAuth2TwitchRepository
){
    fun invoke(clientId: String,
               clientSecret: String,
               code: String,
               grantType: String,
               redirectUri: String): Single<AccessToken> =
        oAuth2TwitchRepository.getToken(clientId, clientSecret, code, grantType, redirectUri)
}

class RevokeAccessTokenUseCase(
    private val oAuth2TwitchRepository: OAuth2TwitchRepository
){
    fun invoke(clientId: String, token:String) : Completable =
        oAuth2TwitchRepository.revokeToken(clientId, token)
}