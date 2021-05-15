package com.davoh.databasemanager.di

import android.app.Application
import com.davoh.databasemanager.GameDatabase
import com.davoh.databasemanager.GameRoomDataSource
import com.davoh.oauth2_twitch.data.LocalGameDataSource
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule{

    @Provides
    @Singleton
    fun databaseProvider(app:Application): GameDatabase = GameDatabase.getDatabase(app)

    @Provides
    fun localGameDataSourceProvider(
        database: GameDatabase
    ):LocalGameDataSource = GameRoomDataSource(database)


}