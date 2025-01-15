package com.webserva.wings.android.geschekt

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ResultDialog(
    showDialog: MutableState<Boolean>,
    players: List<Player>,
    onDismiss: () -> Unit
) {
    // 点数を計算する関数
    fun calculateScore(chips: Int, cards: List<Int>): Int {
        var preVal = 0
        var score = 0
        for (card in cards) {
            if (card - preVal != 1) {
                score += card
            }
            preVal = card
        }
        return chips - score
    }

    if (showDialog.value) {
        // 各プレイヤーの点数を計算し、最も点数が低いプレイヤーを探す
        val playerScores = players.map { player ->
            player to calculateScore(player.chips, player.cards)
        }
        val winner = playerScores.maxByOrNull { it.second }?.first

        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text("結果発表") },
            text = {
                Column {
                    playerScores.forEach { (player, score) ->
                        Text(text = "${player.name}: チップ ${player.chips}枚, カード ${player.cards.joinToString(", ")}, 点数: $score")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    winner?.let {
                        val winnerText = if (it.name == "プレイヤー1") "あなたの勝ち！" else "${it.name}の勝ち！"
                        val winnerColor = if (it.name == "プレイヤー1") Color.Red else Color.Green
                        Text(text = winnerText, fontSize = 18.sp, color = winnerColor)
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    showDialog.value = false
                    onDismiss() // 結果発表を閉じて設定画面に戻る
                }) {
                    Text("OK")
                }
            }
        )
    }
}
