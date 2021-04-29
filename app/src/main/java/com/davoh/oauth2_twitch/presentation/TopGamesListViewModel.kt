package com.davoh.oauth2_twitch.presentation

import androidx.lifecycle.ViewModel
import com.davoh.oauth2_twitch.usecases.GetTopGamesUseCase
import javax.inject.Inject

class TopGamesListViewModel  @Inject constructor(
    private val getTopGamesUseCase: GetTopGamesUseCase
        ): ViewModel() {

        }