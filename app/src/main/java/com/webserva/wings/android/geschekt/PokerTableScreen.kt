package com.webserva.wings.android.geschekt

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.remember
import androidx.compose.runtime.*

@Composable
fun PokerTableScreen(
    viewModel: GameViewModel = viewModel(),
    onBackToSettings: () -> Unit
) {
    val showResultDialog = remember { mutableStateOf(false) }

    if (viewModel.remainingCards.size == 0 && !showResultDialog.value) {
        showResultDialog.value = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 右上に設定に戻るボタン
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.TopEnd)
                .padding(12.dp)
        ) {
            Button(onClick = onBackToSettings) {
                Text("設定に戻る")
            }
        }

        // DebugBox(viewModel)

        // 中央のカード情報を表示
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp),
            contentAlignment = Alignment.Center
        ) {
            CenterInfo(
                remainingCards = viewModel.remainingCards.size,
                currentCard = viewModel.currentCard.value ?: 0,
                stackedChips = viewModel.stackedChips.value
            )
            PlayerPositions(viewModel)
        }

        // 底部にコントロールゾーンを配置
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            ControlZone(
                onStackChips = {
                    viewModel.playerAction(PlayerAction.STACK)
                    viewModel.takeTurns()
                },
                onCollectChips = {
                    viewModel.playerAction(PlayerAction.COLLECT)
                    //viewModel.takeTurns()
                },
                isEnabled = viewModel.isControlEnabled.value,
                hasChips = viewModel.players[viewModel.currentTurnIndex.value].value.chips > 0
            )
        }

        // 残りカードが0で結果発表ダイアログを表示
        if (viewModel.remainingCards.size == 0) {
            viewModel.isGameOver.value = true
            ResultDialog(
                showDialog = showResultDialog,
                players = viewModel.players.map { it.value },
                onDismiss = onBackToSettings
            )
        }
    }
}
