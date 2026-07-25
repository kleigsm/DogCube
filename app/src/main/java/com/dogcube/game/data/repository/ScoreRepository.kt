package com.dogcube.game.data.repository


import com.dogcube.game.data.local.ScoreDao
import com.dogcube.game.model.ScoreRecord
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ScoreRepository @Inject constructor(private val dao: ScoreDao) {
    val topScores: Flow<List<ScoreRecord>> = dao.getTopScores()
    suspend fun saveScore(record: ScoreRecord) = dao.insert(record)
    suspend fun saveScore(playerName: String, score: Int, level: Int, linesCleared: Int, date: String) =
        dao.insert(ScoreRecord(playerName = playerName, score = score, level = level, linesCleared = linesCleared, date = date))
}
