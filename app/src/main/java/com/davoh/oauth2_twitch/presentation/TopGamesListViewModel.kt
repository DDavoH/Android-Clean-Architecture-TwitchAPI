package com.davoh.oauth2_twitch.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.davoh.oauth2_twitch.domain.Game
import com.davoh.oauth2_twitch.presentation.utils.Event
import com.davoh.oauth2_twitch.usecases.GetTopGamesUseCase
import io.reactivex.rxjava3.disposables.CompositeDisposable
import com.davoh.oauth2_twitch.presentation.TopGamesListViewModel.TopGamesNavigation.*


class TopGamesListViewModel(
    private val getTopGamesUseCase: GetTopGamesUseCase
) : ViewModel() {

    private val disposable = CompositeDisposable()

    private val _events = MutableLiveData<Event<TopGamesNavigation>>()
    val events: LiveData<Event<TopGamesNavigation>> get() = _events

    private var isLoading = false

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    fun getTopGames(authHeader: String, clientId: String, after: String) {
        disposable.add(
            getTopGamesUseCase
                .invoke(authHeader, clientId, after)
                .doOnSubscribe { showLoading() }
                .subscribe({ gamelist ->
                    _events.value = Event(ShowTopGamesList(gamelist))
                }, { error ->
                    hideLoading()
                    _events.value = Event(ShowTopGamesError(error))
                })
        )
    }

    private fun showLoading() {
        isLoading = true
        _events.value = Event(ShowLoading)
    }

    private fun hideLoading() {
        isLoading = false
        _events.value = Event(HideLoading)
    }

    sealed class TopGamesNavigation {
        data class ShowTopGamesError(val error: Throwable) : TopGamesNavigation()
        data class ShowTopGamesList(val gameList: List<Game>) : TopGamesNavigation()
        object HideLoading : TopGamesNavigation()
        object ShowLoading : TopGamesNavigation()
    }

}