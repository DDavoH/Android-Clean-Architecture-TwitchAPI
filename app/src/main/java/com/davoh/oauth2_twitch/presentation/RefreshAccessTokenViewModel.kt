package com.davoh.oauth2_twitch.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.davoh.oauth2_twitch.domain.RefreshToken
import com.davoh.oauth2_twitch.presentation.utils.Event
import com.davoh.oauth2_twitch.usecases.RefreshAccessTokenUseCase
import io.reactivex.rxjava3.disposables.CompositeDisposable
import com.davoh.oauth2_twitch.presentation.RefreshAccessTokenViewModel.RefreshAccessTokenNavigation.*

class RefreshAccessTokenViewModel (
    private val refreshAccessTokenUseCase: RefreshAccessTokenUseCase
) : ViewModel() {

    private val disposable = CompositeDisposable()

    private val _events = MutableLiveData<Event<RefreshAccessTokenNavigation>>()
    val events: LiveData<Event<RefreshAccessTokenNavigation>> get() = _events

    private var isLoading = false

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    fun refreshToken(grantType:String, refreshToken:String, clientId:String, clientSecret:String){
        disposable.add(
            refreshAccessTokenUseCase
                .invoke(grantType, refreshToken, clientId, clientSecret)
                .doOnSubscribe{ showLoading()}
                .subscribe({accessToken->
                    _events.value = Event(
                        ShowRefreshAccessToken(accessToken)
                    )
                }, { error->
                    hideLoading()
                    _events.value = Event(
                        ShowRefreshAccessTokenError(
                            error
                        )
                    )
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

    sealed class RefreshAccessTokenNavigation{
        data class ShowRefreshAccessTokenError(val error:Throwable) : RefreshAccessTokenNavigation()
        data class ShowRefreshAccessToken(val token: RefreshToken): RefreshAccessTokenNavigation()
        object HideLoading : RefreshAccessTokenNavigation()
        object ShowLoading : RefreshAccessTokenNavigation()
    }

}