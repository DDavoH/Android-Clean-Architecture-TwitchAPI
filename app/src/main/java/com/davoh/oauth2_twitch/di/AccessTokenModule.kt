package com.davoh.oauth2_twitch.di

import com.davoh.oauth2_twitch.presentation.AccessTokenViewModel
import com.davoh.oauth2_twitch.usecases.GetAccessTokenUseCase
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Module
class AccessTokenModule {

    @Provides
    fun characterListViewModelProvider(
        getAccessTokenUseCase: GetAccessTokenUseCase
    ) = AccessTokenViewModel(
        getAccessTokenUseCase
    )
}

@Subcomponent(modules = [(AccessTokenModule::class)])
interface AccessTokenComponent {
    val accessTokenViewModel: AccessTokenViewModel
}
