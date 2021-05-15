package com.davoh.databasemanager

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game")
data class GameEntity(
    @PrimaryKey @ColumnInfo(name = "game_id") var id: Int,
    @ColumnInfo(name = "game_name") var name:String,
    @ColumnInfo(name = "url_image") var urlImage:String
)