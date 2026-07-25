package com.dogcube.game.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dogcube.game.engine.GameEngine
import com.dogcube.game.engine.GameSnapshot
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch


class GameViewModel : ViewModel() {
    val engine = GameEngine()
    private var tickJob: Job? = null
    val snapshot: StateFlow<GameSnapshot> = engine.snapshot.stateIn(viewModelScope, SharingStarted.Eagerly, engine.snapshot.value)


    fun startGame() { engine.start(); startTicking() }


    private fun startTicking() {
        tickJob?.cancel()
        tickJob = viewModelScope.launch { while (isActive) { delay(engine.dropIntervalMs()); if (!engine.tick()) break } }
    }


    private fun restartTicking() { tickJob?.cancel(); startTicking() }
    fun moveLeft() = engine.moveLeft()
    fun moveRight() = engine.moveRight()
    fun softDrop() { engine.softDrop(); restartTicking() }
    fun hardDrop() { engine.hardDrop(); restartTicking() }
    fun rotateCW() = engine.rotateCW()
    fun rotateCCW() = engine.rotateCCW()
    fun togglePause() { engine.togglePause(); if (engine.phase == com.dogcube.game.engine.GamePhase.PLAYING) restartTicking() else tickJob?.cancel() }
    override fun onCleared() { super.onCleared(); tickJob?.cancel() }
}
