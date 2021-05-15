package com.davoh.databasemanager

import android.content.Context
import androidx.room.*

@Database(entities = [GameEntity::class], version = 1, exportSchema = false)
abstract class GameDatabase : RoomDatabase(){
    abstract fun gameDao(): GameDao

    companion object {
        private const val DATABASE_NAME = "twitch_oauth2_clean_db"

        @Synchronized
        fun getDatabase(context: Context): GameDatabase = Room.databaseBuilder(
            context.applicationContext,
            GameDatabase::class.java,
            DATABASE_NAME
        ).build()
    }
}