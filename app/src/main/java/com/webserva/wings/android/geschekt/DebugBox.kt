package com.webserva.wings.android.geschekt

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// デバッグ情報表示
@Composable
fun DebugBox(viewModel: GameViewModel) {
    Box(
        modifier = Modifier
            .size(150.dp)
            .background(Color.Magenta)
            .padding(8.dp),
        contentAlignment = Alignment.TopStart
    ) {
        Column(horizontalAlignment = Alignment.Start) {
            /*Text(text = "デバッグ情報", color = Color.White, fontSize = 14.sp)
            Text(text = "プレイヤー数: ${viewModel.players.size}", color = Color.White, fontSize = 12.sp)
            Text(text = "残りカード: ${viewModel.remainingCards.size}", color = Color.White, fontSize = 12.sp)
            Text(text = "現在カード: ${viewModel.currentCard.value}", color = Color.White, fontSize = 12.sp)
            Text(text = "積まれたチップ: ${viewModel.stackedChips.value}", color = Color.White, fontSize = 12.sp)
            Text(text = "結果発表表示中: ${viewModel.showResultDialog.value}", color = Color.White, fontSize = 12.sp)
            */
             Text(text = "${viewModel.level.value}")
        }
    }
}

