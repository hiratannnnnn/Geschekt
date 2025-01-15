package com.webserva.wings.android.geschekt

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.sp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

// プレイヤーは一の円形レイアウトと、プレイヤーの詳細表示
@Composable
fun PlayerPositions(viewModel: GameViewModel) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val radiusX = screenWidth / 2.3f
    val radiusY = screenHeight / 3.2f
    val angleStep = 2 * PI / viewModel.players.size

    val boxSize = 100.dp // プレイヤーBoxのサイズ

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        viewModel.players.forEachIndexed { index, player ->
            val angle = if (index == 0) PI / 2 else (index * angleStep + PI / 2)
            var xOffset = cos(angle) * radiusX.value.toDouble()
            val yOffset = sin(angle) * radiusY.value.toDouble()

            // xOffsetが画面の端を超えないように制約をかける
            val maxOffsetX = (screenWidth - boxSize).value.toDouble() / 2
            xOffset = xOffset.coerceIn(-maxOffsetX, maxOffsetX)

            val playerBoxColor = if (index == viewModel.currentTurnIndex.value) {
                Color.Red
            } else {
                Color.LightGray.copy(alpha = 0.4f)
            }
            val animatedColor by animateColorAsState(targetValue = playerBoxColor)

            // スコアを計算
            val score = viewModel.calculateScore(player.value)

            PlayerSeat(
                name = player.value.name,
                chips = player.value.chips,
                cards = player.value.cards,
                score = score,  // スコアを渡す
                modifier = Modifier
                    .offset(xOffset.dp, yOffset.dp)
                    .background(animatedColor),
                isEnabled = (index == viewModel.currentTurnIndex.value)
            )
        }
    }
}


@Composable
fun PlayerSeat(
    name: String,
    chips: Int,
    cards: List<Int>,
    score: Int,  // スコア引数を追加
    modifier: Modifier = Modifier,
    isEnabled: Boolean
) {
    val playerBoxColor = if (isEnabled) Color.LightGray else Color.LightGray.copy(alpha = 0.4f)
    val animatedColor by animateColorAsState(targetValue = playerBoxColor)

    Box(
        modifier = modifier
            .width(120.dp)
            .height(200.dp) // 高さを調整して上下のスペースを確保
            .padding(8.dp)
            .background(animatedColor),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // スコア表示
            Text(text = "Score: $score", fontSize = 14.sp, color = Color.Black)

            Spacer(modifier = Modifier.height(4.dp))

            // チップの表示
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.chip), // chip.pngのリソース
                    contentDescription = "チップアイコン",
                    modifier = Modifier.size(16.dp)
                )
                Text(text = ": $chips", fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // カードの表示（LazyVerticalGridで折り返しを実現）
            LazyVerticalGrid(
                columns = GridCells.Fixed(5), // 1行に5枚表示
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp), // 全体の高さを調整
                contentPadding = PaddingValues(4.dp)
            ) {
                items(cards) { card ->
                    Image(
                        painter = painterResource(id = getCardResource(card)),
                        contentDescription = "カード $card",
                        modifier = Modifier.size(18.dp) // カード画像を小さく設定
                    )
                }
            }
        }
    }
}

// カードのリソースIDを取得する関数
@Composable
fun getCardResource(card: Int): Int {
    val imageName = "card$card"
    return if (card in 3..35) {
        // 必要なカード番号に応じて追加
        R.drawable::class.java.getField(imageName).getInt(null)
        // 必要なカード番号に応じて追加
        } else  R.drawable.card_back // デフォルトのカード画像
}