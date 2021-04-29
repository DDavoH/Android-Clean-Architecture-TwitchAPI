package com.davoh.oauth2_twitch.data

import com.davoh.oauth2_twitch.domain.AccessToken
import com.davoh.oauth2_twitch.domain.Game
import io.reactivex.rxjava3.core.Single

interface RemoteTopGamesDataSource{
    fun getTopGames(authHeader: String,
                     clientId: String,
                     after:String = ""): Single<List<Game>>
}

interface RemoteOAuth2TwitchDataSource{
    fun getToken(clientId: String,
                 clientSecret: String,
                 code: String,
                 grantType: String,
                 redirectUri: String): Single<AccessToken>
}