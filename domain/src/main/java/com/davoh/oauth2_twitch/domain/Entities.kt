package com.davoh.oauth2_twitch.domain

data class TopGames(
    val gameList:List<Game>,
    val pagination: Pagination
)

data class Game (
    val id:Int,
    val name:String,
    val urlImage:String
)

data class Pagination(
    val cursor:String
)

data class AccessToken(
val accessToken:String,
val expiresIn: Int,
val refreshToken:String,
val scopeList:List<String>,
val tokenType:String
)

data class RefreshToken(
    val accessToken: String,
    val refreshToken: String,
    val scopeList:List<String>
)