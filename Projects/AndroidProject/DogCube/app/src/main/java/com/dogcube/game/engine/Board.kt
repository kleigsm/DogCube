package com.dogcube.game.engine

class Board {

    companion object {
        const val WIDTH = 10
        const val HEIGHT = 20
    }

    /** grid[y][x]: 0 = empty, >0 = DogBreed ordinal + 1 */
    val grid: Array<IntArray> = Array(HEIGHT) { IntArray(WIDTH) { 0 } }

    fun clear() {
        for (y in 0 until HEIGHT) {
            for (x in 0 until WIDTH) {
                grid[y][x] = 0
            }
        }
    }

    /** Lock a piece shape onto the board. Returns count of cleared lines. */
    fun lock(breedOrdinal: Int, shape: Array<IntArray>, x: Int, y: Int): Int {
        val cellValue = breedOrdinal + 1
        for (row in shape.indices) {
            for (col in shape[row].indices) {
                if (shape[row][col] == 0) continue
                val boardY = y + row
                val boardX = x + col
                if (boardY in 0 until HEIGHT && boardX in 0 until WIDTH) {
                    grid[boardY][boardX] = cellValue
                }
            }
        }
        return clearFullLines()
    }

    /** Remove complete rows, shift above rows down. Returns number of lines cleared. */
    fun clearFullLines(): Int {
        var cleared = 0
        var y = HEIGHT - 1
        while (y >= 0) {
            if (grid[y].all { it != 0 }) {
                // Shift all rows above down by one
                for (shiftY in y downTo 1) {
                    grid[shiftY] = grid[shiftY - 1].copyOf()
                }
                grid[0] = IntArray(WIDTH) { 0 }
                cleared++
                // Don't decrement y — check the newly shifted row
            } else {
                y--
            }
        }
        return cleared
    }

    /** Check if any locked cell exists above the visible area (row < 0 conceptually). */
    fun isTopBlocked(): Boolean {
        for (x in 0 until WIDTH) {
            if (grid[0][x] != 0) return true
        }
        return false
    }
}
