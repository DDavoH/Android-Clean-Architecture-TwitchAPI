package com.davoh.oauth2_twitch.data.di

import com.davoh.oauth2_twitch.data.OAuth2TwitchRepository
import com.davoh.oauth2_twitch.data.RemoteOAuth2TwitchDataSource
import com.davoh.oauth2_twitch.data.RemoteTopGamesDataSource
import com.davoh.oauth2_twitch.data.TopGamesRepository
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {

    @Provides
    fun providesTopGamesRepository(
        remoteTopGamesDataSource: RemoteTopGamesDataSource
    ) = TopGamesRepository(remoteTopGamesDataSource)

    @Provides
    fun providesOAuth2TwitchRepository(
        remoteOAuth2TwitchDataSource: RemoteOAuth2TwitchDataSource
    ) = OAuth2TwitchRepository(remoteOAuth2TwitchDataSource)

}