package com.davoh.oauth2_twitch.di

import com.davoh.oauth2_twitch.presentation.AccessTokenViewModel
import com.davoh.oauth2_twitch.presentation.RevokeAccessTokenViewModel
import com.davoh.oauth2_twitch.usecases.GetAccessTokenUseCase
import com.davoh.oauth2_twitch.usecases.RevokeAccessTokenUseCase
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Module
class AccessTokenModule {

    @Provides
    fun accessTokenViewModelProvider(
        getAccessTokenUseCase: GetAccessTokenUseCase
    ) = AccessTokenViewModel(getAccessTokenUseCase)

    @Provides
    fun revokeAccessTokenViewModelProvider(
        revokeAccessTokenUseCase: RevokeAccessTokenUseCase
    ) = RevokeAccessTokenViewModel(revokeAccessTokenUseCase)

}

@Subcomponent(modules = [(AccessTokenModule::class)])
interface AccessTokenComponent {
    val accessTokenViewModel: AccessTokenViewModel
    val revokeAccessTokenViewModel : RevokeAccessTokenViewModel
}
