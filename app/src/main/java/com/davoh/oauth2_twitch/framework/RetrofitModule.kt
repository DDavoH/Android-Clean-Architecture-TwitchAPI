package com.davoh.oauth2_twitch.framework

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RetrofitModule {

    @Singleton
    @Provides
    fun providesTwitchOAuth2(): TwitchOAuth2{
        return TwitchOAuth2.create()
    }

    @Singleton
    @Provides
    fun providesTwitchAPI():TwitchAPI{
        return TwitchAPI.create()
    }


}