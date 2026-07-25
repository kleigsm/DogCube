package com.dogcube.game.engine

import org.junit.Assert.*
import org.junit.Test

class BoardTest {

    @Test
    fun boardIsEmptyInitially() {
        val board = Board()
        for (y in 0 until Board.HEIGHT) {
            for (x in 0 until Board.WIDTH) {
                assertEquals("Cell ($x,$y) should be 0", 0, board.grid[y][x])
            }
        }
    }

    @Test
    fun lockAndClearSingleLine() {
        val board = Board()
        val shape = PieceType.I.shapes[0] // horizontal I: fills row 0
        // Fill almost full bottom row except last cell
        for (x in 0 until 9) {
            board.grid[Board.HEIGHT - 1][x] = 1
        }
        // Now lock the I piece to fill the last 4 cells (x=6-9)
        val cleared = board.lock(0, shape, 6, Board.HEIGHT - 1)
        assertEquals(1, cleared)
        // Top of board should still be empty
        assertArrayEquals(IntArray(Board.WIDTH) { 0 }, board.grid[0])
    }

    @Test
    fun clearMultipleLines() {
        val board = Board()
        // Fill 4 rows fully
        for (y in Board.HEIGHT - 4 until Board.HEIGHT) {
            for (x in 0 until Board.WIDTH) {
                board.grid[y][x] = 1
            }
        }
        val cleared = board.clearFullLines()
        assertEquals(4, cleared)
    }

    @Test
    fun noClearWhenNoFullLines() {
        val board = Board()
        board.grid[Board.HEIGHT - 1][0] = 1
        val cleared = board.clearFullLines()
        assertEquals(0, cleared)
    }

    @Test
    fun clearPreservesColumns() {
        val board = Board()
        for (x in 0 until Board.WIDTH) {
            board.grid[Board.HEIGHT - 1][x] = 1
        }
        // Row 18: partially filled
        board.grid[Board.HEIGHT - 2][3] = 2
        val cleared = board.clearFullLines()
        assertEquals(1, cleared)
        // Row 19 (was 18) should still have cell 3 filled
        assertEquals(2, board.grid[Board.HEIGHT - 1][3])
    }
}
