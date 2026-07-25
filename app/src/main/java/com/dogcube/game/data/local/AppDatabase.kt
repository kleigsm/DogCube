package com.dogcube.game.data.local


import androidx.room.Database
import androidx.room.RoomDatabase
import com.dogcube.game.model.ScoreRecord


@Database(entities = [ScoreRecord::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun scoreDao(): ScoreDao
}
