package com.davoh.oauth2_twitch.framework.requestmanager

import com.davoh.oauth2_twitch.data.RemoteOAuth2TwitchDataSource
import com.davoh.oauth2_twitch.data.RemoteTopGamesDataSource
import com.davoh.oauth2_twitch.domain.AccessToken
import com.davoh.oauth2_twitch.domain.Game
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class TopGamesTwitchRetrofitDataSource(
    private val topGamesTwitchRequest:TopGamesTwitchRequest
): RemoteTopGamesDataSource{

    override fun getTopGames(
        authHeader: String,
        clientId: String,
        after: String
    ): Single<List<Game>> {
        return topGamesTwitchRequest
            .getService<TwitchTopGamesService>()
            .getTopGames(authHeader,clientId,after)
            .map(TopGamesResponse::toDomainGameList)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
    }
}

class OAuth2TwitchDataSource(
    private val oAuth2TwitchRequest: OAuth2TwitchRequest
):RemoteOAuth2TwitchDataSource {

    override fun getToken(
        clientId: String,
        clientSecret: String,
        code: String,
        grantType: String,
        redirectUri: String
    ): Single<AccessToken> {
        return oAuth2TwitchRequest
            .getService<TwitchOAuth2Service>()
            .getToken(clientId,clientSecret,code, grantType, redirectUri)
            .map(AccessTokenResponse::toDomainAccessToken)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
    }

}