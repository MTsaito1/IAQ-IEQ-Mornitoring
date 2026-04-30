package com.example.iaqieqmornitoring.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import kotlin.math.roundToInt

@Composable
fun MiniGauge(
    value: Float,
    label: String,
    size: Dp = 120.dp // 🔥 fix ขนาดตรงนี้
) {
    val percent = value.coerceIn(0f, 100f)

    val color = when {
        percent < 40 -> Color(0xFF00E676)
        percent < 70 -> Color(0xFFFFC107)
        else -> Color(0xFFFF5252)
    }

    Box(
        modifier = Modifier.size(size),
        contentAlignment = Alignment.Center
    ) {

        Canvas(modifier = Modifier.fillMaxSize()) {

            val stroke = 12.dp.toPx()

            // background
            drawArc(
                color = Color.DarkGray,
                startAngle = 135f,
                sweepAngle = 270f,
                useCenter = false,
                style = Stroke(stroke)
            )

            // value
            drawArc(
                color = color,
                startAngle = 135f,
                sweepAngle = 270f * (percent / 100f),
                useCenter = false,
                style = Stroke(stroke, cap = StrokeCap.Round)
            )
        }

        // 🔥 TEXT ซ้อนในเกจ (ไม่ใช่ Canvas)
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            MiniGauge(
                value = percent,
                label = label
            )
        }
    }
}