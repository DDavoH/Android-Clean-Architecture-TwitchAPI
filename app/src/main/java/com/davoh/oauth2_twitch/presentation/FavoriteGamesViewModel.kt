package com.davoh.oauth2_twitch.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.davoh.oauth2_twitch.domain.Game
import com.davoh.oauth2_twitch.presentation.utils.Event
import com.davoh.oauth2_twitch.usecases.GetAllFavoriteGameUseCase
import com.davoh.oauth2_twitch.usecases.GetFavoriteGameStatusUseCase
import com.davoh.oauth2_twitch.usecases.UpdateFavoriteGameStatusUseCase
import io.reactivex.rxjava3.disposables.CompositeDisposable
import com.davoh.oauth2_twitch.presentation.FavoriteGamesViewModel.FavoriteGamesNavigation.*
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class FavoriteGamesViewModel(
    private val getAllFavoriteGameUseCase: GetAllFavoriteGameUseCase,
    private val getFavoriteGameStatusUseCase: GetFavoriteGameStatusUseCase,
    private val updateFavoriteGameStatusUseCase: UpdateFavoriteGameStatusUseCase,
) : ViewModel() {

    private val disposable = CompositeDisposable()

    private val _events = MutableLiveData<Event<FavoriteGamesNavigation>>()
    val events: LiveData<Event<FavoriteGamesNavigation>> get() = _events

    val favoriteGamesList:LiveData<List<Game>>
        get() = LiveDataReactiveStreams.fromPublisher(getAllFavoriteGameUseCase.invoke())

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> get() = _isFavorite

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    fun getAllFavoriteGames(){
        disposable.add(
            getAllFavoriteGameUseCase
                .invoke()
                .subscribe({favoriteGameList->
                    _events.value = Event(ShowFavoriteGameList(favoriteGameList))
                }, {error->
                    _events.value = Event(ShowFavoriteGameListError(error))
                })
        )
    }

    fun updateFavoriteGameStatus(game:Game){
        disposable.add(
            updateFavoriteGameStatusUseCase
                .invoke(game)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    _events.value = Event(UpdatedFavoriteGame)
                },{
                    _events.value = Event(UpdateFavoriteGameError)
                })
        )
    }

    fun validateFavoriteGameStatus(gameId:Int){
        disposable.add(
            getFavoriteGameStatusUseCase
                .invoke(gameId)
                .subscribe{
                    isFavorite ->
                    _isFavorite.value = isFavorite
                }
        )
    }

    sealed class FavoriteGamesNavigation{
        data class ShowFavoriteGameListError(val error: Throwable) : FavoriteGamesNavigation()
        data class ShowFavoriteGameList(val favoriteGames:List<Game>) : FavoriteGamesNavigation()
        object UpdatedFavoriteGame : FavoriteGamesNavigation()
        object UpdateFavoriteGameError :FavoriteGamesNavigation()
    }
}