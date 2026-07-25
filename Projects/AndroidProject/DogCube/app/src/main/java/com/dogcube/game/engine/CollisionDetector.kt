package com.dogcube.game.engine

object CollisionDetector {

    /** Check if placing [shape] at ([x],[y]) on [board] causes any collision. */
    fun collides(
        board: Array<IntArray>,
        shape: Array<IntArray>,
        x: Int,
        y: Int,
        boardWidth: Int = Board.WIDTH,
        boardHeight: Int = Board.HEIGHT
    ): Boolean {
        for (row in shape.indices) {
            for (col in shape[row].indices) {
                if (shape[row][col] == 0) continue
                val boardX = x + col
                val boardY = y + row
                // Out of bounds
                if (boardX < 0 || boardX >= boardWidth || boardY >= boardHeight) return true
                // Above ceiling is allowed (y < 0)
                if (boardY < 0) continue
                // Occupied cell
                if (board[boardY][boardX] != 0) return true
            }
        }
        return false
    }

    /** Ghost piece Y: lowest valid Y position for hard drop. */
    fun ghostY(
        board: Array<IntArray>,
        shape: Array<IntArray>,
        x: Int,
        y: Int,
        boardWidth: Int = Board.WIDTH,
        boardHeight: Int = Board.HEIGHT
    ): Int {
        var gy = y
        while (!collides(board, shape, x, gy + 1, boardWidth, boardHeight)) {
            gy++
        }
        return gy
    }
}
