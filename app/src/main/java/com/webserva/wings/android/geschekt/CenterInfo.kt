package com.webserva.wings.android.geschekt

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ContentScale

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.webserva.wings.android.geschekt.R

@Composable
fun CenterInfo(
    remainingCards: Int,
    currentCard: Int?,
    stackedChips: Int
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.background_wood),
            contentDescription = "Background",
            modifier = Modifier.size(200.dp),
            contentScale = ContentScale.FillBounds
        )
        Column(
                horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
        // サイズを少し広めに調整
            // .background(Color.Black)
            /*.background(
                Brush.linearGradient(
                    colors = listOf(Color(92,45,39), Color.White),
                    start = Offset(0f,0f),
                    end = Offset(1000f, 1000f)
                )
            )*/
        ) {
            // 上部に残りカード枚数
            Text(
                text = "残り: $remainingCards",
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 4.dp).background(Color.White.copy(alpha = 0.8f)),

            )

            // 表カードと裏カードを横に並べて表示
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                // 表カード
                if (currentCard != null) {
                    val imageName = "card$currentCard"
                    Image(
                        painter = painterResource(id = when (currentCard) {
                            in 3..35 -> R.drawable::class.java.getField(imageName).getInt(null) // 実際のカード画像リソースに変更
                            else -> R.drawable.chip
                        }),
                        contentDescription = "Current Card",
                        modifier = Modifier.size(80.dp),
                        contentScale = ContentScale.Fit
                    )
                }

                // 裏カード
                Image(
                    painter = painterResource(id = R.drawable.card_back), // 裏向きカードの画像リソース
                    contentDescription = "Card Back",
                    modifier = Modifier.size(80.dp),
                    contentScale = ContentScale.Fit
                )
            }

            // 下部に積まれたチップの数
            Text(
                text = "チップ: $stackedChips",
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 4.dp).background(Color.White.copy(alpha = 0.8f))
            )

            ChipDisplay(chipCount = stackedChips)
        }
    }
}