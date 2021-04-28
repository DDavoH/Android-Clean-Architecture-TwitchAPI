package com.davoh.oauth2_twitch.framework.mappers

import com.davoh.oauth2_twitch.domain.model.Game
import com.davoh.oauth2_twitch.framework.responses.GameResponse

fun List<GameResponse>.toDomainGameList(): List<Game> = this.map {
    val stringValue = it.boxArtUtl
    val urlImage = stringValue.replace("{width}x{height}","144x192")
    Game(it.id, it.name, urlImage)
}