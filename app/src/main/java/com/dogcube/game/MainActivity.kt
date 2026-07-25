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
import com.dogcube.game.ui.screens.GameScreen
import com.dogcube.game.ui.screens.HomeScreen
import com.dogcube.game.ui.screens.ScoreScreen
import com.dogcube.game.ui.theme.DogCubeTheme
import com.dogcube.game.viewmodel.GameViewModel
import dagger.hilt.android.AndroidEntryPoint
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
        setContent { DogCubeTheme { DogCubeApp() } }
    }


    @Composable
    private fun DogCubeApp() {
        var screen by remember { mutableStateOf(AppScreen.HOME) }
        val gameVM: GameViewModel = hiltViewModel()
        val scores by scoreRepository.topScores.collectAsStateWithLifecycle(initialValue = emptyList())
        val snap by gameVM.snapshot.collectAsState()


        LaunchedEffect(snap.phase) {
            if (snap.phase == GamePhase.GAME_OVER && snap.score > 0) {
                val df = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                scoreRepository.saveScore("Íæ¼Ò", snap.score, snap.level, snap.linesCleared, df.format(Date()))
            }
        }


        when (screen) {
            AppScreen.HOME -> HomeScreen(
                onStartGame = { screen = AppScreen.GAME; gameVM.startGame() },
                onShowScores = { screen = AppScreen.SCORES }
            )
            AppScreen.GAME -> {
                GameScreen(gameVM, onBackToMenu = { screen = AppScreen.HOME })
            }
            AppScreen.SCORES -> ScoreScreen(scores, onBack = { screen = AppScreen.HOME })
        }
    }
}
