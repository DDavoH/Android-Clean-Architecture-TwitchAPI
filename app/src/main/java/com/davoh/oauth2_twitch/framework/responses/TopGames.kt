package com.davoh.oauth2_twitch.framework.responses

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

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