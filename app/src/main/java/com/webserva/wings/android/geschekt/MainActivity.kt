package com.webserva.wings.android.geschekt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.webserva.wings.android.geschekt.ui.theme.GeschektTheme
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GeschektTheme {
                var isGameStarted by remember { mutableStateOf(false) }
                val viewModel: GameViewModel = viewModel()

                if (isGameStarted) {
                    // ゲーム画面を表示
                    PokerTableScreen(
                        viewModel = viewModel,
                        onBackToSettings = { isGameStarted = false } // 設定に戻る
                    )
                } else {
                    // ゲーム設定画面を表示
                    GameSettingsScreen(
                        onStartGame = { playerCount, level, first, last, jokers ->
                            viewModel.initializeGame(playerCount, level, first, last, jokers) // プレイヤー数に基づきゲームを初期化
                            isGameStarted = true // ゲーム開始フラグを設定
                        }
                    )
                }
            }
        }
    }
}