package com.davoh.oauth2_twitch.framework

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AccessToken(
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
):Parcelable