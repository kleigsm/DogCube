package com.dogcube.game.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dogcube.game.audio.SoundManager
import com.dogcube.game.audio.SoundType
import com.dogcube.game.engine.GameEngine
import com.dogcube.game.engine.GamePhase
import com.dogcube.game.engine.GameSnapshot
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val soundManager: SoundManager
) : ViewModel() {

    val engine = GameEngine()
    private var tickJob: Job? = null

    val snapshot: StateFlow<GameSnapshot> = engine.snapshot
        .stateIn(viewModelScope, SharingStarted.Eagerly, engine.snapshot.value)

    fun startGame() {
        engine.start()
        soundManager.load()
        startTicking()
    }

    private fun startTicking() {
        tickJob?.cancel()
        tickJob = viewModelScope.launch {
            while (isActive) {
                delay(engine.dropIntervalMs())
                val prevLines = engine.linesCleared
                val prevPhase = engine.phase
                if (!engine.tick()) break
                val cleared = engine.linesCleared - prevLines
                when {
                    cleared >= 4 -> soundManager.play(SoundType.TETRIS)
                    cleared in 1..3 -> soundManager.play(SoundType.LINE_CLEAR)
                }
                if (engine.phase == GamePhase.GAME_OVER && prevPhase != GamePhase.GAME_OVER) {
                    soundManager.play(SoundType.GAME_OVER)
                }
            }
        }
    }

    private fun restartTicking() {
        tickJob?.cancel()
        startTicking()
    }

    fun moveLeft() {
        if (engine.moveLeft()) soundManager.play(SoundType.MOVE)
    }

    fun moveRight() {
        if (engine.moveRight()) soundManager.play(SoundType.MOVE)
    }

    fun softDrop() {
        if (engine.softDrop()) {
            soundManager.play(SoundType.SOFT_DROP)
        }
        restartTicking()
    }

    fun hardDrop() {
        engine.hardDrop()
        soundManager.play(SoundType.HARD_DROP)
        restartTicking()
    }

    fun rotateCW() {
        engine.rotateCW()
        soundManager.play(SoundType.ROTATE)
    }

    fun rotateCCW() {
        engine.rotateCCW()
        soundManager.play(SoundType.ROTATE)
    }

    fun togglePause() {
        engine.togglePause()
        if (engine.phase == GamePhase.PLAYING) {
            restartTicking()
        } else {
            tickJob?.cancel()
        }
    }

    override fun onCleared() {
        super.onCleared()
        tickJob?.cancel()
        soundManager.release()
    }
}
