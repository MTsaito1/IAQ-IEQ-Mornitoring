package com.example.iaqieqmornitoring.utils

import androidx.compose.ui.graphics.vector.ImageVector
data class SensorItem(
    val title: String,
    val value: Float,
    val unit: String,
    val range: List<Float>, // ✅ ต้องเป็น List
    val icon: ImageVector
)
