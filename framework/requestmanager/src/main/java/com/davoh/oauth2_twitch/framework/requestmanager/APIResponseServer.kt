package com.davoh.oauth2_twitch.framework.requestmanager

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AccessTokenResponse(
    @SerializedName("access_token")
    val accessToken:String,
    @SerializedName("expires_in")
    val expiresIn: Int,
    @SerializedName("refresh_token")
    val refreshToken:String,
    @SerializedName("scope")
    val scopeList:List<String>,
    @SerializedName("token_type")
    val tokenType:String
): Parcelable


@Parcelize
data class TopGamesResponse(
    @SerializedName("data")
    val gameList:List<GameResponse>,
    @SerializedName("pagination")
    val pagination: PaginationResponse
): Parcelable

@Parcelize
data class GameResponse(
    @SerializedName("id")
    val id:Int,
    @SerializedName("name")
    val name:String,
    @SerializedName("box_art_url")
    val boxArtUtl:String
): Parcelable

@Parcelize
data class PaginationResponse(
    @SerializedName("cursor")
    val cursor:String
): Parcelable