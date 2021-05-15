package com.davoh.databasemanager

import com.davoh.oauth2_twitch.data.LocalGameDataSource
import com.davoh.oauth2_twitch.domain.Game
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.schedulers.Schedulers

class GameRoomDataSource(
    database:GameDatabase
): LocalGameDataSource {

    private val gameDao by lazy {database.gameDao()}

    override fun getAllFavoriteGames(): Flowable<List<Game>> = gameDao
            .getAllFavoriteGames()
            .map(List<GameEntity>::toGameDomainList)
            .onErrorReturn{ emptyList()}
            .subscribeOn(Schedulers.io())

    override fun getFavoriteGameStatus(gameId: Int): Maybe<Boolean> {
        return gameDao.getGameById(gameId)
            .isEmpty
            .flatMapMaybe { isEmpty->
                Maybe.just(!isEmpty)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
    }

    override fun updateGameStatus(game: Game): Maybe<Boolean> {
        val gameEntity = game.toGameEntity()
        return gameDao.getGameById(gameEntity.id)
            .isEmpty
            .flatMapMaybe { isEmpty->
                if(isEmpty){
                    gameDao.insertGame(gameEntity)
                }else{
                    gameDao.deleteGame(gameEntity)
                }
                Maybe.just(isEmpty)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
    }
}