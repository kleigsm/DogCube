package com.dogcube.game.engine


class Board {
    companion object { const val WIDTH = 10; const val HEIGHT = 20 }
    val grid: Array<IntArray> = Array(HEIGHT) { IntArray(WIDTH) { 0 } }


    fun clear() { for (y in 0 until HEIGHT) for (x in 0 until WIDTH) grid[y][x] = 0 }


    fun lock(breedOrdinal: Int, shape: Array<IntArray>, x: Int, y: Int): Int {
        val v = breedOrdinal + 1
        for (row in shape.indices) for (col in shape[row].indices) {
            if (shape[row][col] == 0) continue
            val by = y + row; val bx = x + col
            if (by in 0 until HEIGHT && bx in 0 until WIDTH) grid[by][bx] = v
        }
        return clearFullLines()
    }


    fun clearFullLines(): Int {
        var cleared = 0; var y = HEIGHT - 1
        while (y >= 0) {
            if (grid[y].all { it != 0 }) {
                for (sy in y downTo 1) grid[sy] = grid[sy - 1].copyOf()
                grid[0] = IntArray(WIDTH) { 0 }; cleared++
            } else y--
        }
        return cleared
    }
}
