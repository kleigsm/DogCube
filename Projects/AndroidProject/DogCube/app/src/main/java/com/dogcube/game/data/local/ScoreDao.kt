package com.dogcube.game.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dogcube.game.model.ScoreRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface ScoreDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: ScoreRecord)

    @Query("SELECT * FROM scores ORDER BY score DESC LIMIT 20")
    fun getTopScores(): Flow<List<ScoreRecord>>
}
