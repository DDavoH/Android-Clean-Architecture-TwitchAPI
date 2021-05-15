package com.davoh.databasemanager

import androidx.room.*
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe


@Dao
interface GameDao{
    @Query("SELECT * FROM game")
    fun getAllFavoriteGames(): Flowable<List<GameEntity>>

    @Query("SELECT * FROM game WHERE game_id = :id")
    fun getGameById(id:Int): Maybe<GameEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGame(gameEntity: GameEntity)

    @Delete
    fun deleteGame(gameEntity: GameEntity)
}