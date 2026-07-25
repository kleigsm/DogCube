package com.dogcube.game.engine

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class GameSnapshot(
    val board: List<List<Int>>,
    val currentPiece: PieceType?,
    val currentX: Int,
    val currentY: Int,
    val currentRotation: Int,
    val nextPiece: PieceType?,
    val ghostY: Int,
    val score: Int,
    val level: Int,
    val linesCleared: Int,
    val phase: GamePhase
)

enum class GamePhase { READY, PLAYING, PAUSED, GAME_OVER }

class GameEngine {

    private val board = Board()
    private val generator = PieceGenerator()

    private val _snapshot = MutableStateFlow(
        GameSnapshot(
            board = emptyList(),
            currentPiece = null,
            currentX = 0, currentY = 0,
            currentRotation = 0,
            nextPiece = null,
            ghostY = 0,
            score = 0, level = 1, linesCleared = 0,
            phase = GamePhase.READY
        )
    )
    val snapshot: StateFlow<GameSnapshot> = _snapshot.asStateFlow()

    var score = 0; private set
    var level = 1; private set
    var linesCleared = 0; private set
    var phase = GamePhase.READY; private set

    private var currentPiece: PieceType? = null
    private var currentX = 0
    private var currentY = 0
    private var currentRotation = 0
    private var nextPiece: PieceType? = null
    private var isRunning = false

    /** Spawn position: piece centered horizontally, top of board. */
    fun spawnX(piece: PieceType) = (Board.WIDTH - piece.shapes[0][0].size) / 2

    /** Start a new game. */
    fun start() {
        board.clear()
        generator.reset()
        score = 0
        level = 1
        linesCleared = 0

        // Generate first two pieces
        val first = generator.next()
        nextPiece = generator.next()
        spawn(first)

        phase = GamePhase.PLAYING
        isRunning = true
        emit()
    }

    /** Tick: move piece down by gravity. If blocked, lock and spawn next. */
    fun tick(): Boolean {
        if (phase != GamePhase.PLAYING) return false
        val piece = currentPiece ?: return false

        if (!tryMove(0, 1)) {
            lockCurrent()
        }
        emit()
        return phase == GamePhase.PLAYING
    }

    /** Move left/right. Returns true on success. */
    fun moveLeft(): Boolean = tryMove(-1, 0)
    fun moveRight(): Boolean = tryMove(1, 0)

    /** Soft drop: move down 1, award 1 point. Returns true on success. */
    fun softDrop(): Boolean {
        if (phase != GamePhase.PLAYING) return false
        return if (tryMove(0, 1)) {
            score += 1
            emit()
            true
        } else false
    }

    /** Hard drop: drop to ghost Y, lock immediately. */
    fun hardDrop() {
        if (phase != GamePhase.PLAYING) return
        val piece = currentPiece ?: return
        val dist = ghostY() - currentY
        currentY = ghostY()
        score += dist * 2
        lockCurrent()
    }

    /** Rotate clockwise (next state). */
    fun rotateCW() = rotate((currentRotation + 1) % 4)

    /** Rotate counter-clockwise (prev state). */
    fun rotateCCW() = rotate((currentRotation + 3) % 4)

    fun togglePause() {
        when (phase) {
            GamePhase.PLAYING -> phase = GamePhase.PAUSED
            GamePhase.PAUSED -> phase = GamePhase.PLAYING
            else -> {}
        }
        emit()
    }

    fun stop() {
        isRunning = false
    }

    // --- Private ---

    private fun spawn(piece: PieceType) {
        currentPiece = piece
        currentX = spawnX(piece)
        currentY = -1 // start above visible board
        currentRotation = 0

        if (CollisionDetector.collides(board.grid, piece.shapes[0], currentX, currentY)) {
            phase = GamePhase.GAME_OVER
        }
    }

    private fun tryMove(dx: Int, dy: Int): Boolean {
        val piece = currentPiece ?: return false
        val shape = piece.shapes[currentRotation]
        if (CollisionDetector.collides(board.grid, shape, currentX + dx, currentY + dy)) return false
        currentX += dx
        currentY += dy
        emit()
        return true
    }

    private fun rotate(toState: Int) {
        val piece = currentPiece ?: return
        val result = RotationSystem.tryRotate(board.grid, piece, currentRotation, toState, currentX, currentY)
        if (result != null) {
            currentX = result.first
            currentY = result.second
            currentRotation = toState
        }
        emit()
    }

    private fun lockCurrent() {
        val piece = currentPiece ?: return
        val cleared = board.lock(piece.breed.ordinal, piece.shapes[currentRotation], currentX, currentY)
        linesCleared += cleared
        addScore(cleared)
        updateLevel()

        val next = nextPiece!!
        nextPiece = generator.next()
        spawn(next)
        emit()
    }

    private fun addScore(lines: Int) {
        val base = when (lines) {
            1 -> 100
            2 -> 300
            3 -> 500
            4 -> 800
            else -> 0
        }
        score += base * level
    }

    private fun updateLevel() {
        level = 1 + linesCleared / 10
    }

    /** Fall interval in milliseconds. */
    fun dropIntervalMs(): Long {
        // Start at 1000ms, decrease by 75ms per level, minimum 100ms
        val ms = 1000L - (level - 1) * 75L
        return ms.coerceAtLeast(100L)
    }

    fun ghostY(): Int {
        val piece = currentPiece ?: return currentY
        return CollisionDetector.ghostY(board.grid, piece.shapes[currentRotation], currentX, currentY)
    }

    private fun emit() {
        _snapshot.value = GameSnapshot(
            board = board.grid.map { it.toList() },
            currentPiece = currentPiece,
            currentX = currentX, currentY = currentY,
            currentRotation = currentRotation,
            nextPiece = nextPiece,
            ghostY = ghostY(),
            score = score, level = level, linesCleared = linesCleared,
            phase = phase
        )
    }
}
