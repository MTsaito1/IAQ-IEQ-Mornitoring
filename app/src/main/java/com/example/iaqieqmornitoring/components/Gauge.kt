package com.example.iaqieqmornitoring.components

import androidx.compose.runtime.*
import androidx.compose.foundation.clickable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.runtime.getValue
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.*

@Composable
fun IAQGauge(
    iaqValue: Int,
    ieqValue: Int,
    level: Int
) {
    var isIAQ by remember { mutableStateOf(true) }

    val value = if (isIAQ) iaqValue else ieqValue
    val label = if (isIAQ) "IAQ" else "IEQ"

    val statusList = listOf("Good","Moderate","Polluted","Very Polluted","Severe")
    val status = statusList[level]

    val color = when(level){
        0 -> Color(0xFF5BBFA7)
        1 -> Color(0xFF00E676)
        2 -> Color(0xFFFFEA00)
        3 -> Color(0xFFFF9100)
        else -> Color(0xFFFF1744)
    }

    Box(
        modifier = Modifier.size(260.dp),
        contentAlignment = Alignment.Center
    ) {

        Canvas(modifier = Modifier.fillMaxSize()) {

            val stroke = 30f

            drawArc(
                color = Color(0xFF1F2937),
                startAngle = 135f,
                sweepAngle = 270f,
                useCenter = false,
                style = Stroke(stroke)
            )

            drawArc(
                color = color,
                startAngle = 135f,
                sweepAngle = (value / 100f) * 270f,
                useCenter = false,
                style = Stroke(stroke)
            )
        }

        // 🔥 CENTER (กดได้)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .clickable { isIAQ = !isIAQ }   // 👈 กดสลับตรงนี้
        ) {

            // 🔹 ปุ่มเล็กด้านบน (เหมือนในแบบ)
            Box(
                modifier = Modifier
                    .background(
                        color = Color(0xFF1E293B),
                        shape = RoundedCornerShape(50)
                    )
                    .padding(horizontal = 14.dp, vertical = 4.dp)
            ) {
                Text(label, color = Color.White, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "$value",
                color = color,
                fontSize = 52.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = status,
                color = color,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun IAQLegend() {
    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        LegendItem("Good", Color(0xFF5BBFA7))
        LegendItem("Moderate", Color(0xFF00E676))
        LegendItem("Polluted", Color(0xFFFFEA00))
        LegendItem("Very Polluted", Color(0xFFFF9100))
        LegendItem("Severe", Color(0xFFFF1744))
    }
}

@Composable
fun LegendItem(text: String, color: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .background(color, shape = CircleShape)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = text,
            color = color,
            fontSize = 14.sp
        )
    }
}

