package com.dogcube.game.data.local

import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(application: android.app.Application): AppDatabase {
        return Room.databaseBuilder(
            application,
            AppDatabase::class.java,
            "dogcube.db"
        ).build()
    }

    @Provides
    fun provideScoreDao(database: AppDatabase): ScoreDao {
        return database.scoreDao()
    }
}
