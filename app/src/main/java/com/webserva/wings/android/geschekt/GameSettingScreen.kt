package com.webserva.wings.android.geschekt

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GameSettingsScreen(
    onStartGame: (Int, Int, Int, Int, Int) -> Unit // プレイヤー数、レベル、first、last、jokersを受け取る
) {
    var playerCount by remember { mutableIntStateOf(4) } // デフォルトは4人
    var level by remember { mutableIntStateOf(1) } // デフォルトはレベル1
    var first by remember { mutableIntStateOf(3) } // デフォルトは3
    var last by remember { mutableIntStateOf(35) } // デフォルトは35
    var jokers by remember { mutableIntStateOf(9) } // デフォルトは9
    var showAdvancedSettings by remember { mutableStateOf(false) } // 詳細設定の表示切替

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // プレイヤー人数の設定
        Text("プレイヤー人数を選択", fontSize = 20.sp)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Button(
                onClick = { if (playerCount > 2) playerCount-- },
                enabled = playerCount > 2
            ) {
                Text("－")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text("$playerCount 人", fontSize = 18.sp)
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = { if (playerCount < 7) playerCount++ },
                enabled = playerCount < 7
            ) {
                Text("＋")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // レベルの設定
        Text("レベルを選択", fontSize = 20.sp)
        Slider(
            value = level.toFloat(),
            onValueChange = { level = it.toInt() },
            valueRange = 1f..5f,
            steps = 4
        )
        Text("レベル $level")

        Spacer(modifier = Modifier.height(16.dp))

        // 詳細設定の表示切替ボタン
        Button(
            modifier = Modifier.width(80.dp).height(30.dp),
            onClick = { showAdvancedSettings = !showAdvancedSettings }
        ) {
            Text("詳細")
        }

        // 詳細設定フィールド
        if (showAdvancedSettings) {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                // first設定
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("First", fontSize = 14.sp)
                    TextField(
                        value = first.toString(),
                        onValueChange = { newValue -> first = newValue.toIntOrNull() ?: first },
                        modifier = Modifier.width(60.dp)
                    )
                }
                // last設定
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Last", fontSize = 14.sp)
                    TextField(
                        value = last.toString(),
                        onValueChange = { newValue -> last = newValue.toIntOrNull() ?: last },
                        modifier = Modifier.width(60.dp)
                    )
                }
                // jokers設定
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Jokers", fontSize = 14.sp)
                    TextField(
                        value = jokers.toString(),
                        onValueChange = { newValue -> jokers = newValue.toIntOrNull() ?: jokers },
                        modifier = Modifier.width(60.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // スタートボタン
        Button(onClick = { onStartGame(playerCount, level, first, last, jokers) }) {
            Text("スタート")
        }
    }
}