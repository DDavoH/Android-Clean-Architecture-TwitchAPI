package com.davoh.oauth2_twitch.usecases.di

import com.davoh.oauth2_twitch.data.OAuth2TwitchRepository
import com.davoh.oauth2_twitch.data.TopGamesRepository
import com.davoh.oauth2_twitch.usecases.GetAccessTokenUseCase
import com.davoh.oauth2_twitch.usecases.GetTopGamesUseCase
import com.davoh.oauth2_twitch.usecases.RefreshAccessTokenUseCase
import com.davoh.oauth2_twitch.usecases.RevokeAccessTokenUseCase
import dagger.Module
import dagger.Provides

@Module
class UseCaseModule {

    @Provides
    fun getTopGamesUseCaseProvider(topGamesRepository: TopGamesRepository) =
        GetTopGamesUseCase(topGamesRepository)

    @Provides
    fun getAccessTokenUseCaseProvider(oAuth2TwitchRepository: OAuth2TwitchRepository) =
        GetAccessTokenUseCase(oAuth2TwitchRepository)

    @Provides
    fun revokeAccessTokenUseCaseProvider(oAuth2TwitchRepository: OAuth2TwitchRepository) =
        RevokeAccessTokenUseCase(oAuth2TwitchRepository)

    @Provides
    fun refreshAccessTokenUseCaseProvider(oAuth2TwitchRepository: OAuth2TwitchRepository) =
        RefreshAccessTokenUseCase(oAuth2TwitchRepository)

}