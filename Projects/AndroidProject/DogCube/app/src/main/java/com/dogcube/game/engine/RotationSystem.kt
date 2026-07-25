package com.dogcube.game.engine

object RotationSystem {

    /** SRS wall kick offsets for J/L/S/T/Z pieces (3x3 bounding box). */
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

    /** SRS wall kick offsets for I piece (4x4 bounding box). */
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
        board: Array<IntArray>,
        piece: PieceType,
        fromState: Int,
        toState: Int,
        x: Int,
        y: Int
    ): Pair<Int, Int>? {
        val key = "$fromState>$toState"
        val kicks = when (piece) {
            PieceType.O -> return Pair(x, y) // O never changes
            PieceType.I -> wallKicksI[key]
            else -> wallKicksJLSTZ[key]
        } ?: return null

        val newShape = piece.shapes[toState]
        for ((dx, dy) in kicks) {
            val newX = x + dx
            val newY = y - dy // SRS uses screen coords: +dy = up
            if (!CollisionDetector.collides(board, newShape, newX, newY)) {
                return Pair(newX, newY)
            }
        }
        return null
    }
}
