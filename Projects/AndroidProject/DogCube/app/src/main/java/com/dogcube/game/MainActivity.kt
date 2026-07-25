package com.dogcube.game

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dogcube.game.data.repository.ScoreRepository
import com.dogcube.game.engine.GamePhase
import com.dogcube.game.model.AppScreen
import com.dogcube.game.model.ScoreRecord
import com.dogcube.game.ui.screens.GameScreen
import com.dogcube.game.ui.screens.HomeScreen
import com.dogcube.game.ui.screens.ScoreScreen
import com.dogcube.game.ui.theme.DogCubeTheme
import com.dogcube.game.viewmodel.GameViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var scoreRepository: ScoreRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DogCubeTheme {
                DogCubeApp()
            }
        }
    }

    @Composable
    private fun DogCubeApp() {
        var currentScreen by remember { mutableStateOf(AppScreen.HOME) }
        val gameViewModel: GameViewModel = hiltViewModel()
        val scores by scoreRepository.topScores.collectAsStateWithLifecycle(initialValue = emptyList())
        val coroutineScope = rememberCoroutineScope()
        val snap by gameViewModel.snapshot.collectAsState()

        // Handle game over: auto-save score
        LaunchedEffect(snap.phase) {
            if (snap.phase == GamePhase.GAME_OVER && snap.score > 0) {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                scoreRepository.saveScore(
                    playerName = "Player",
                    score = snap.score,
                    level = snap.level,
                    linesCleared = snap.linesCleared,
                    date = dateFormat.format(Date())
                )
            }
        }

        when (currentScreen) {
            AppScreen.HOME -> {
                HomeScreen(
                    onStartGame = {
                        currentScreen = AppScreen.GAME
                        gameViewModel.startGame()
                    },
                    onShowScores = { currentScreen = AppScreen.SCORES }
                )
            }
            AppScreen.GAME -> {
                GameScreen(
                    viewModel = gameViewModel,
                    onGameOver = {} // handled by LaunchedEffect + screen switch below
                )

                // Auto-return to home after game over with delay
                LaunchedEffect(snap.phase) {
                    if (snap.phase == GamePhase.GAME_OVER) {
                        kotlinx.coroutines.delay(2000)
                        currentScreen = AppScreen.HOME
                    }
                }
            }
            AppScreen.SCORES -> {
                ScoreScreen(
                    scores = scores,
                    onBack = { currentScreen = AppScreen.HOME }
                )
            }
        }
    }
}
