package com.davoh.oauth2_twitch.framework

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class StoreToken(
    @SerializedName("response_type")
    val responseType:String,
    @SerializedName("client_id")
    val clientId:String,
    @SerializedName("redirect_uri")
    val redirectURI:String,
    @SerializedName("scope")
    val scope:String,
    @SerializedName("state")
    val state:String
):Parcelable