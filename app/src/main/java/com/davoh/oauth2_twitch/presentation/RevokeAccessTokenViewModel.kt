package com.davoh.oauth2_twitch.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.davoh.oauth2_twitch.presentation.utils.Event
import com.davoh.oauth2_twitch.usecases.RevokeAccessTokenUseCase
import io.reactivex.rxjava3.disposables.CompositeDisposable
import com.davoh.oauth2_twitch.presentation.RevokeAccessTokenViewModel.RevokeAccessTokenNavigation.*

class RevokeAccessTokenViewModel (
    private val revokeAccessTokenUseCase: RevokeAccessTokenUseCase
) : ViewModel() {

    private val disposable = CompositeDisposable()

    private val _events = MutableLiveData<Event<RevokeAccessTokenNavigation>>()
    val events: LiveData<Event<RevokeAccessTokenNavigation>> get() = _events

    private var isLoading = false

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    fun revokeAccessToken(clientId: String, token:String){
        disposable.add(
            revokeAccessTokenUseCase
                .invoke(clientId, token)
                .doOnSubscribe{showLoading()}
                .subscribe({
                    hideLoading()
                    _events.value = Event(ShowRevokeAccessToken)
                 },{ error->
                    hideLoading()
                    _events.value = Event(ShowRevokeAccessTokenError(error))
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


    sealed class RevokeAccessTokenNavigation{
        data class ShowRevokeAccessTokenError(val error:Throwable): RevokeAccessTokenNavigation()
        object ShowRevokeAccessToken:RevokeAccessTokenNavigation()
        object HideLoading : RevokeAccessTokenNavigation()
        object ShowLoading : RevokeAccessTokenNavigation()
    }
}