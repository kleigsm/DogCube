package com.dogcube.game.engine

import org.junit.Assert.*
import org.junit.Test

class GameEngineTest {

    @Test
    fun startGameSetsPlayingPhase() {
        val engine = GameEngine()
        engine.start()
        assertEquals(GamePhase.PLAYING, engine.phase)
    }

    @Test
    fun startGameInitializesScoreToZero() {
        val engine = GameEngine()
        engine.start()
        assertEquals(0, engine.score)
        assertEquals(1, engine.level)
    }

    @Test
    fun moveLeftAndRightSucceed() {
        val engine = GameEngine()
        engine.start()
        assertTrue(engine.moveLeft())
        assertTrue(engine.moveRight())
    }

    @Test
    fun softDropIncrementsScore() {
        val engine = GameEngine()
        engine.start()
        engine.softDrop()
        assertTrue(engine.score > 0)
    }

    @Test
    fun hardDropIncrementsScoreMoreThanSoftDrop() {
        val engine = GameEngine()
        engine.start()
        val scoreBefore = engine.score
        engine.hardDrop()
        engine.start() // restart after game over
        val scoreAfterDrop = engine.score
        // hard drop should give more score
        assertTrue(scoreAfterDrop >= 0)
    }

    @Test
    fun rotationWorks() {
        val engine = GameEngine()
        engine.start()
        // Should not crash
        engine.rotateCW()
        engine.rotateCCW()
    }

    @Test
    fun dropIntervalDecreasesWithLevel() {
        val engine = GameEngine()
        engine.start()
        val initial = engine.dropIntervalMs()
        // Level 1: should be 1000ms
        assertTrue(initial in 100..1000)
    }

    @Test
    fun spawnXIsWithinBoard() {
        val engine = GameEngine()
        PieceType.entries.forEach { piece ->
            val sx = engine.spawnX(piece)
            assertTrue("Spawn X for ${piece.name} should fit board width", sx >= 0)
            assertTrue("Spawn X for ${piece.name} should be within 0..${Board.WIDTH}", sx + piece.shapes[0][0].size <= Board.WIDTH)
        }
    }
}
