package com.davoh.oauth2_twitch.domain

data class Game (
    val id:Int,
    val name:String,
    val urlImage:String
)

data class AccessToken(
val accessToken:String,
val expiresIn: Int,
val refreshToken:String,
val scopeList:List<String>,
val tokenType:String
)