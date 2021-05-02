package com.davoh.oauth2_twitch.framework.requestmanager

import com.davoh.oauth2_twitch.domain.AccessToken
import com.davoh.oauth2_twitch.domain.Game
import com.davoh.oauth2_twitch.domain.Pagination
import com.davoh.oauth2_twitch.domain.TopGames

fun TopGamesResponse.toDomainGameList(): List<Game> = this.gameList.toDomainGameList()

fun List<GameResponse>.toDomainGameList(): List<Game> = this.map {
    val stringValue = it.boxArtUtl
    val urlImage = stringValue.replace("{width}x{height}","144x192")
    Game(it.id, it.name, urlImage)
}

fun AccessTokenResponse.toDomainAccessToken(): AccessToken =
    AccessToken(accessToken, expiresIn, refreshToken, scopeList,tokenType)

fun TopGamesResponse.toDomainTopGames(): TopGames {
    val topGameList = gameList.map{
        val stringValue = it.boxArtUtl
        val urlImage = stringValue.replace("{width}x{height}","144x192")
        Game(it.id,it.name,urlImage)
    }
    val paginationDomain = Pagination(pagination.cursor)
    return TopGames(topGameList,paginationDomain)
}


