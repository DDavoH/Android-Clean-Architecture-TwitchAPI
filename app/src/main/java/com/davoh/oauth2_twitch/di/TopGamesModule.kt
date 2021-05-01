package com.davoh.oauth2_twitch.di

import com.davoh.oauth2_twitch.presentation.TopGamesListViewModel
import com.davoh.oauth2_twitch.usecases.GetTopGamesUseCase
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Module
class TopGamesModule {

    @Provides
    fun topGamesListViewModelProvider(
        getTopGamesUseCase: GetTopGamesUseCase
    ) = TopGamesListViewModel(getTopGamesUseCase)

}

@Subcomponent(modules = [(TopGamesModule::class)])
interface TopGamesComponent {
    val topGamesListViewModel: TopGamesListViewModel
}
