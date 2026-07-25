package com.dogcube.game.engine


import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


data class GameSnapshot(
    val board: List<List<Int>>,
    val currentPiece: PieceType?,
    val currentX: Int, val currentY: Int, val currentRotation: Int,
    val nextPiece: PieceType?, val ghostY: Int,
    val score: Int, val level: Int, val linesCleared: Int,
    val phase: GamePhase
)


enum class GamePhase { READY, PLAYING, PAUSED, GAME_OVER }


class GameEngine {
    private val board = Board()
    private val generator = PieceGenerator()
    private val _snapshot = MutableStateFlow(
        GameSnapshot(emptyList(), null, 0, 0, 0, null, 0, 0, 1, 0, GamePhase.READY)
    )
    val snapshot: StateFlow<GameSnapshot> = _snapshot.asStateFlow()


    var score = 0; private set
    var level = 1; private set
    var linesCleared = 0; private set
    var phase = GamePhase.READY; private set


    private var currentPiece: PieceType? = null
    private var currentX = 0; private var currentY = 0; private var currentRotation = 0
    private var nextPiece: PieceType? = null


    fun spawnX(piece: PieceType) = (Board.WIDTH - piece.shapes[0][0].size) / 2


    fun start() {
        board.clear(); generator.reset(); score = 0; level = 1; linesCleared = 0
        currentPiece = generator.next(); nextPiece = generator.next()
        currentX = spawnX(currentPiece!!); currentY = -1; currentRotation = 0
        phase = if (CollisionDetector.collides(board.grid, currentPiece!!.shapes[0], currentX, currentY)) GamePhase.GAME_OVER else GamePhase.PLAYING
        emit()
    }


    fun tick(): Boolean {
        if (phase != GamePhase.PLAYING) return false
        if (!tryMove(0, 1)) lockCurrent()
        emit(); return phase == GamePhase.PLAYING
    }


    fun moveLeft(): Boolean { val r = tryMove(-1, 0); emit(); return r }
    fun moveRight(): Boolean { val r = tryMove(1, 0); emit(); return r }


    fun softDrop(): Boolean {
        if (phase != GamePhase.PLAYING) return false
        return if (tryMove(0, 1)) { score += 1; emit(); true } else false
    }


    fun hardDrop() {
        if (phase != GamePhase.PLAYING) return
        val p = currentPiece ?: return
        val dist = ghostY() - currentY; currentY = ghostY(); score += dist * 2; lockCurrent()
    }


    fun rotateCW() = rotate((currentRotation + 1) % 4)
    fun rotateCCW() = rotate((currentRotation + 3) % 4)


    fun togglePause() { phase = when(phase) { GamePhase.PLAYING -> GamePhase.PAUSED; GamePhase.PAUSED -> GamePhase.PLAYING; else -> phase }; emit() }


    fun dropIntervalMs(): Long = (1000L - (level - 1) * 75L).coerceAtLeast(100L)
    fun ghostY(): Int { val p = currentPiece ?: return currentY; return CollisionDetector.ghostY(board.grid, p.shapes[currentRotation], currentX, currentY) }


    private fun tryMove(dx: Int, dy: Int): Boolean {
        val p = currentPiece ?: return false
        if (CollisionDetector.collides(board.grid, p.shapes[currentRotation], currentX + dx, currentY + dy)) return false
        currentX += dx; currentY += dy; return true
    }


    private fun rotate(toState: Int) {
        val p = currentPiece ?: return
        val r = RotationSystem.tryRotate(board.grid, p, currentRotation, toState, currentX, currentY) ?: return
        currentX = r.first; currentY = r.second; currentRotation = toState; emit()
    }


    private fun lockCurrent() {
        val p = currentPiece ?: return
        val c = board.lock(p.breed.ordinal, p.shapes[currentRotation], currentX, currentY)
        linesCleared += c
        score += when(c) { 1 -> 100; 2 -> 300; 3 -> 500; 4 -> 800; else -> 0 } * level
        level = 1 + linesCleared / 10
        currentPiece = nextPiece!!; nextPiece = generator.next()
        currentX = spawnX(currentPiece!!); currentY = -1; currentRotation = 0
        if (CollisionDetector.collides(board.grid, currentPiece!!.shapes[0], currentX, currentY)) phase = GamePhase.GAME_OVER
        emit()
    }


    private fun emit() {
        _snapshot.value = GameSnapshot(
            board.grid.map { it.toList() }, currentPiece, currentX, currentY, currentRotation,
            nextPiece, ghostY(), score, level, linesCleared, phase
        )
    }
}
