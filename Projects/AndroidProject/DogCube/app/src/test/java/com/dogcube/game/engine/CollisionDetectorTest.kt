package com.dogcube.game.engine

import org.junit.Assert.*
import org.junit.Test

class CollisionDetectorTest {

    private val board = Array(Board.HEIGHT) { IntArray(Board.WIDTH) { 0 } }

    @Test
    fun noCollisionInEmptyBoard() {
        assertFalse(CollisionDetector.collides(board, PieceType.T.shapes[0], 3, 0))
    }

    @Test
    fun collisionAtLeftBoundary() {
        assertTrue(CollisionDetector.collides(board, PieceType.T.shapes[0], -1, 0))
    }

    @Test
    fun collisionAtRightBoundary() {
        assertTrue(CollisionDetector.collides(board, PieceType.T.shapes[0], 8, 0))
    }

    @Test
    fun collisionAtBottomBoundary() {
        board[Board.HEIGHT - 1][5] = 1
        // Place T piece near bottom so it overlaps
        assertTrue(CollisionDetector.collides(board, PieceType.T.shapes[0], 3, Board.HEIGHT - 2))
    }

    @Test
    fun ghostYDropsToFloor() {
        val gy = CollisionDetector.ghostY(board, PieceType.I.shapes[0], 3, 0)
        assertTrue(gy > 0)
        // Should land just above bottom
        assertTrue(CollisionDetector.collides(board, PieceType.I.shapes[0], 3, gy + 1))
        assertFalse(CollisionDetector.collides(board, PieceType.I.shapes[0], 3, gy))
    }

    @Test
    fun aboveCeilingAllowed() {
        // y = -1, piece partially above board — should be valid
        assertFalse(CollisionDetector.collides(board, PieceType.T.shapes[0], 3, -1))
    }
}
