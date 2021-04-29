package com.davoh.oauth2_twitch.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.davoh.oauth2_twitch.domain.AccessToken
import com.davoh.oauth2_twitch.presentation.utils.Event
import com.davoh.oauth2_twitch.usecases.GetAccessTokenUseCase
import io.reactivex.rxjava3.disposables.CompositeDisposable
import com.davoh.oauth2_twitch.presentation.AccessTokenNavigation.*
import javax.inject.Inject

class AccessTokenViewModel @Inject constructor(
    private val getAccessTokenUseCase: GetAccessTokenUseCase
) : ViewModel() {

    private val disposable = CompositeDisposable()

    private val _events = MutableLiveData<Event<AccessTokenNavigation>>()
    val events: LiveData<Event<AccessTokenNavigation>> get() = _events

    private var isLoading = false

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
    

    fun getAccessToken(clientId:String, clientSecret:String, code:String, grantType:String, redirectUri:String){
       disposable.add(
           getAccessTokenUseCase
               .invoke(clientId, clientSecret, code, grantType, redirectUri)
               .doOnSubscribe{ showLoading()}
               .subscribe({accessToken->
                   _events.value = Event(ShowAccessToken(accessToken))
               }, { error->
                   hideLoading()
                   _events.value = Event(ShowAccessTokenError(error))
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


}

sealed class AccessTokenNavigation{
    data class ShowAccessTokenError(val error:Throwable) : AccessTokenNavigation()
    data class ShowAccessToken(val token:AccessToken): AccessTokenNavigation()
    object HideLoading : AccessTokenNavigation()
    object ShowLoading : AccessTokenNavigation()
}


