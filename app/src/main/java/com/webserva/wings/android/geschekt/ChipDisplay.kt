package com.webserva.wings.android.geschekt

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun ChipDisplay(chipCount: Int) {
    val rows = (chipCount + 9) / 10 // 10枚ごとの行数を計算
    val maxRows = 4 // 最大で表示する行数

    Box(
        modifier = Modifier
            .height((maxRows * 16).dp) // 表示エリアの高さを固定
            .width(160.dp) // 必要に応じて幅も調整
            .padding(start = 16.dp)
            //.verticalScroll(rememberScrollState()) // 行数が多い場合にスクロール可能にする
    ) {
        Column(
            horizontalAlignment = Alignment.Start
        ) {
            for (i in 0 until rows) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val chipsInThisRow = if (i == rows - 1 && chipCount % 10 != 0) chipCount % 10 else 10
                    for (j in 0 until chipsInThisRow) {
                        Image(
                            painter = painterResource(id = R.drawable.chip), // チップの画像リソースを指定
                            contentDescription = "Chip",
                            modifier = Modifier
                                .size(16.dp) // チップのサイズ
                                .offset(x = -(j * 3).dp) // 重なり具合
                        )
                    }
                }
            }
        }
    }
}
