package com.davoh.oauth2_twitch.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.davoh.oauth2_twitch.domain.TopGames
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
    private var isLastPage = false

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    fun onLoadMoreItems(visibleItemCount: Int, firstVisibleItemPosition: Int, totalItemCount: Int, authHeader: String, clientId: String, after: String,before:String) {
        if (isLoading || isLastPage || !isInFooter(visibleItemCount, firstVisibleItemPosition, totalItemCount)) {
            return
        }
        getTopGames(authHeader, clientId, after, before)

    }

    fun onRetryGetAllCharacter(itemCount: Int, authHeader: String, clientId: String, after: String,before:String) {
        if (itemCount > 0) {
            _events.value = Event(HideLoading)
            return
        }

        getTopGames(authHeader, clientId, after, before)
    }

    fun getTopGames(authHeader: String, clientId: String, after: String,before:String) {
        disposable.add(
            getTopGamesUseCase
                .invoke(authHeader, clientId, after, before, PAGE_SIZE)
                .doOnSubscribe { showLoading() }
                .subscribe({ topgames ->
                    if (topgames.gameList.size < PAGE_SIZE) {
                        isLastPage = true
                    }
                    hideLoading()
                    _events.value = Event(ShowTopGamesList(topgames))
                }, { error ->
                    isLastPage = true
                    hideLoading()
                    _events.value = Event(ShowTopGamesError(error))
                })
        )
    }

    private fun isInFooter(
        visibleItemCount: Int,
        firstVisibleItemPosition: Int,
        totalItemCount: Int
    ): Boolean {
        return visibleItemCount + firstVisibleItemPosition >= totalItemCount
                && firstVisibleItemPosition >= 0
                && totalItemCount >= PAGE_SIZE
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
        data class ShowTopGamesList(val topGames: TopGames) : TopGamesNavigation()
        object HideLoading : TopGamesNavigation()
        object ShowLoading : TopGamesNavigation()
    }

    companion object {

        private const val PAGE_SIZE = 20
    }

}