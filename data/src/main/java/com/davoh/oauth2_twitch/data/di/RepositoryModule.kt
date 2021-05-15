package com.davoh.oauth2_twitch.data.di

import com.davoh.oauth2_twitch.data.*
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {

    @Provides
    fun providesTopGamesRepository(
        remoteTopGamesDataSource: RemoteTopGamesDataSource,
        localGameDataSource: LocalGameDataSource
    ) = TopGamesRepository(remoteTopGamesDataSource, localGameDataSource)

    @Provides
    fun providesOAuth2TwitchRepository(
        remoteOAuth2TwitchDataSource: RemoteOAuth2TwitchDataSource
    ) = OAuth2TwitchRepository(remoteOAuth2TwitchDataSource)

}