package com.davoh.oauth2_twitch.di

import com.davoh.oauth2_twitch.presentation.FavoriteGamesViewModel
import com.davoh.oauth2_twitch.presentation.TopGamesListViewModel
import com.davoh.oauth2_twitch.usecases.GetAllFavoriteGameUseCase
import com.davoh.oauth2_twitch.usecases.GetFavoriteGameStatusUseCase
import com.davoh.oauth2_twitch.usecases.GetTopGamesUseCase
import com.davoh.oauth2_twitch.usecases.UpdateFavoriteGameStatusUseCase
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Module
class TopGamesModule {

    @Provides
    fun topGamesListViewModelProvider(
        getTopGamesUseCase: GetTopGamesUseCase
    ) = TopGamesListViewModel(getTopGamesUseCase)

    @Provides
    fun favoriteGamesViewModelProvider(
        getAllFavoriteGameUseCase: GetAllFavoriteGameUseCase,
        getFavoriteGameStatusUseCase: GetFavoriteGameStatusUseCase,
        updateFavoriteGameStatusUseCase: UpdateFavoriteGameStatusUseCase
    ) = FavoriteGamesViewModel(getAllFavoriteGameUseCase,
    getFavoriteGameStatusUseCase, updateFavoriteGameStatusUseCase)

}

@Subcomponent(modules = [(TopGamesModule::class)])
interface TopGamesComponent {
    val topGamesListViewModel: TopGamesListViewModel
    val favoriteGamesViewModel: FavoriteGamesViewModel
}
