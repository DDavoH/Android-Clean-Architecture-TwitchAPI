package com.davoh.databasemanager

import com.davoh.oauth2_twitch.domain.Game

fun List<GameEntity>.toGameDomainList() = map(GameEntity::toGameDomain)

fun GameEntity.toGameDomain() = Game(id, name, urlImage)

fun Game.toGameEntity()= GameEntity(id,name, urlImage)