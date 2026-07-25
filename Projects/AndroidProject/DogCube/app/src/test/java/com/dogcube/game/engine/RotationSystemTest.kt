package com.dogcube.game.engine

import org.junit.Assert.*
import org.junit.Test

class RotationSystemTest {

    private val board = Array(Board.HEIGHT) { IntArray(Board.WIDTH) { 0 } }

    @Test
    fun rotateCWReturnsNewPosition() {
        val result = RotationSystem.tryRotate(board, PieceType.T, 0, 1, 3, 5)
        assertNotNull(result)
    }

    @Test
    fun oPieceRotationReturnsSamePosition() {
        val result = RotationSystem.tryRotate(board, PieceType.O, 0, 1, 3, 5)
        assertEquals(Pair(3, 5), result)
    }

    @Test
    fun rotationBlockedByWallReturnsNull() {
        val walled = Array(Board.HEIGHT) { IntArray(Board.WIDTH) { 0 } }
        // Fill entire right wall
        for (y in 0 until Board.HEIGHT) {
            walled[y][Board.WIDTH - 1] = 1
        }
        // T piece at x=7 (right edge), try rotation
        val result = RotationSystem.tryRotate(walled, PieceType.T, 0, 1, 7, 10)
        // Should succeed via wall kick
        assertNotNull(result)
    }

    @Test
    fun fullRotationCycleReturnsToOriginal() {
        var x = 3; var y = 10; var state = 0
        for (i in 0 until 4) {
            val result = RotationSystem.tryRotate(board, PieceType.T, state, (state + 1) % 4, x, y)
            assertNotNull("Rotation $i should succeed", result)
            x = result!!.first
            y = result.second
            state = (state + 1) % 4
        }
        assertEquals(0, state)
    }
}
