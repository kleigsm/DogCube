package com.dogcube.game.engine


object RotationSystem {


    private val wallKicksJLSTZ = mapOf(
        "0>1" to listOf(Pair(0,0), Pair(-1,0), Pair(-1,1), Pair(0,-2), Pair(-1,-2)),
        "1>0" to listOf(Pair(0,0), Pair(1,0), Pair(1,-1), Pair(0,2), Pair(1,2)),
        "1>2" to listOf(Pair(0,0), Pair(1,0), Pair(1,-1), Pair(0,2), Pair(1,2)),
        "2>1" to listOf(Pair(0,0), Pair(-1,0), Pair(-1,1), Pair(0,-2), Pair(-1,-2)),
        "2>3" to listOf(Pair(0,0), Pair(1,0), Pair(1,1), Pair(0,-2), Pair(1,-2)),
        "3>2" to listOf(Pair(0,0), Pair(-1,0), Pair(-1,-1), Pair(0,2), Pair(-1,2)),
        "3>0" to listOf(Pair(0,0), Pair(-1,0), Pair(-1,-1), Pair(0,2), Pair(-1,2)),
        "0>3" to listOf(Pair(0,0), Pair(1,0), Pair(1,1), Pair(0,-2), Pair(1,-2))
    )


    private val wallKicksI = mapOf(
        "0>1" to listOf(Pair(0,0), Pair(-2,0), Pair(1,0), Pair(-2,-1), Pair(1,2)),
        "1>0" to listOf(Pair(0,0), Pair(2,0), Pair(-1,0), Pair(2,1), Pair(-1,-2)),
        "1>2" to listOf(Pair(0,0), Pair(-1,0), Pair(2,0), Pair(-1,2), Pair(2,-1)),
        "2>1" to listOf(Pair(0,0), Pair(1,0), Pair(-2,0), Pair(1,-2), Pair(-2,1)),
        "2>3" to listOf(Pair(0,0), Pair(2,0), Pair(-1,0), Pair(2,1), Pair(-1,-2)),
        "3>2" to listOf(Pair(0,0), Pair(-2,0), Pair(1,0), Pair(-2,-1), Pair(1,2)),
        "3>0" to listOf(Pair(0,0), Pair(1,0), Pair(-2,0), Pair(1,-2), Pair(-2,1)),
        "0>3" to listOf(Pair(0,0), Pair(-1,0), Pair(2,0), Pair(-1,2), Pair(2,-1))
    )


    fun tryRotate(
        board: Array<IntArray>, piece: PieceType,
        fromState: Int, toState: Int, x: Int, y: Int
    ): Pair<Int, Int>? {
        if (piece == PieceType.O) return Pair(x, y)
        val key = "fromState>toState"
        val kicks = if (piece == PieceType.I) wallKicksI[key] else wallKicksJLSTZ[key] ?: return null
        val shape = piece.shapes[toState]
        for ((dx, dy) in kicks) {
            val nx = x + dx; val ny = y - dy
            if (!CollisionDetector.collides(board, shape, nx, ny)) return Pair(nx, ny)
        }
        return null
    }
}
