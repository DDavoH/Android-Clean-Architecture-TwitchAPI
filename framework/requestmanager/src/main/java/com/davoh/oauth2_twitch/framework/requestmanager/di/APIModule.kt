package com.davoh.oauth2_twitch.framework.requestmanager.di

import com.davoh.oauth2_twitch.framework.requestmanager.APIConstants.BASE_API_OAUTH2_URL
import com.davoh.oauth2_twitch.framework.requestmanager.APIConstants.BASE_API_URL
import com.davoh.oauth2_twitch.framework.requestmanager.OAuth2TwitchRequest
import com.davoh.oauth2_twitch.framework.requestmanager.TopGamesTwitchRequest
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
class APIModule {

    @Provides
    @Singleton
    @Named("baseUrl")
    fun baseUrlProvider(): String = BASE_API_URL

    @Provides
    @Singleton
    @Named("baseOAuth2Url")
    fun baseOAuth2UrlProvider(): String = BASE_API_OAUTH2_URL

    @Provides
    fun oAuth2TwitchRequestProvider(
        @Named("baseOAuth2Url") baseUrl: String
    ) = OAuth2TwitchRequest(baseUrl)

    @Provides
    fun topGamesTwitchRequestProvider(
        @Named("baseUrl") baseUrl: String
    ) = TopGamesTwitchRequest(baseUrl)


}