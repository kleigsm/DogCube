package com.dogcube.game.engine


object CollisionDetector {


    fun collides(
        board: Array<IntArray>,
        shape: Array<IntArray>,
        x: Int, y: Int,
        boardWidth: Int = Board.WIDTH,
        boardHeight: Int = Board.HEIGHT
    ): Boolean {
        for (row in shape.indices) {
            for (col in shape[row].indices) {
                if (shape[row][col] == 0) continue
                val bx = x + col
                val by = y + row
                if (bx < 0 || bx >= boardWidth || by >= boardHeight) return true
                if (by < 0) continue
                if (board[by][bx] != 0) return true
            }
        }
        return false
    }


    fun ghostY(
        board: Array<IntArray>,
        shape: Array<IntArray>,
        x: Int, y: Int,
        boardWidth: Int = Board.WIDTH,
        boardHeight: Int = Board.HEIGHT
    ): Int {
        var gy = y
        while (!collides(board, shape, x, gy + 1, boardWidth, boardHeight)) gy++
        return gy
    }
}
