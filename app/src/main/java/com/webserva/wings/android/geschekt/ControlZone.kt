package com.webserva.wings.android.geschekt

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// 操作ゾーンに配置するボタン
@Composable
fun ControlZone(
    onStackChips: () -> Unit,
    onCollectChips: () -> Unit,
    isEnabled: Boolean,
    hasChips: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(80.dp)
            .background(Color.Gray),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val stackButtonColor = if (isEnabled && hasChips) Color.Blue else Color.Blue.copy(alpha = 0.4f)
        val collectButtonColor = if (isEnabled) Color.Red else Color.Red.copy(alpha = 0.4f)
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(stackButtonColor)
                .padding(8.dp)
                .clickable(enabled = isEnabled && hasChips) { onStackChips() },
            contentAlignment = Alignment.Center
        ) {
            Text(text = "チップを積む", color = Color.White, fontSize = 18.sp)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(collectButtonColor)
                .padding(8.dp)
                .clickable(enabled = isEnabled) { onCollectChips() },
            contentAlignment = Alignment.Center
        ) {
            Text(text = "回収する", color = Color.White, fontSize = 18.sp)
        }
    }
}
