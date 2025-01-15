package com.webserva.wings.android.geschekt
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.webserva.wings.android.geschekt.R

@Composable
fun CenterCard(cardNumber: Int) {
    // 画像リソースIDを動的に生成
    val imageName = "card$cardNumber" // 例: "card_3", "card_4" などのリソース名
    val resourceId = when (cardNumber) {
        in 3..35 -> R.drawable::class.java.getField(imageName).getInt(null)
        else -> R.drawable.chip // チップのデフォルト表示
    }
    // 白背景を指定するためのBoxを使って画像の背景色を白にする
    Box(
        modifier = Modifier
            .size(150.dp) // サイズ指定（エラーが出る場合は削除して調整）
            .background(Color.White)
    ) {
        Image(
            painter = painterResource(id = resourceId),
            contentDescription = "Card $cardNumber",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxSize() // Boxに合わせて画像を表示
                .size(16.dp)
        )
    }
}
