package com.example.iaqieqmornitoring.utils

import androidx.compose.ui.graphics.Color
import com.example.iaqieqmornitoring.screens.isLowGood

enum class IAQLevel {
    GOOD,
    MODERATE,
    POLLUTED,
    VERY_POLLUTED,
    SEVERELY_POLLUTED
}
fun getLevelFromScore(score: Float): IAQLevel {
    return when {
        score >= 81 -> IAQLevel.GOOD
        score >= 61 -> IAQLevel.MODERATE
        score >= 41 -> IAQLevel.POLLUTED
        score >= 21 -> IAQLevel.VERY_POLLUTED
        else -> IAQLevel.SEVERELY_POLLUTED
    }
}

fun getColorFromLevel(level: IAQLevel): Color {
    return when (level) {
        IAQLevel.GOOD -> Color(0xFF5BBFA7)              // เขียว
        IAQLevel.MODERATE -> Color(0xFF00E676)          // เหลือง
        IAQLevel.POLLUTED -> Color(0xFFFFEA00)          // ส้ม
        IAQLevel.VERY_POLLUTED -> Color(0xFFFF9100)     // แดง
        IAQLevel.SEVERELY_POLLUTED -> Color(0xFFFF1744) // ม่วง
    }
}

fun getLabel(level: IAQLevel): String {
    return when (level) {
        IAQLevel.GOOD -> "Good"
        IAQLevel.MODERATE -> "Moderate"
        IAQLevel.POLLUTED -> "Polluted"
        IAQLevel.VERY_POLLUTED -> "Very Polluted"
        IAQLevel.SEVERELY_POLLUTED -> "Severely Polluted"
    }
}

fun calculateIAQPercentage(
    value: Float,
    range: List<Float>,
    isLowGood: Boolean
): Float {

    val (a, b, c, d) = range

    return if (isLowGood) {
        // 🔥 ยิ่งน้อยยิ่งดี (CO2, PM2.5, etc.)
        when {
            value <= a -> 100f
            value <= b -> 75f + (b - value) / (b - a) * 25f
            value <= c -> 50f + (c - value) / (c - b) * 25f
            value <= d -> 25f + (d - value) / (d - c) * 25f
            else -> 0f
        }
    } else {
        // 🔥 ช่วงดีที่สุดอยู่กลาง (TEMP, HUMID)
        when {
            value < a -> 0f
            value <= b -> (value - a) / (b - a) * 50f
            value <= c -> 50f + (c - value) / (c - b) * 50f
            value <= d -> 50f - (value - c) / (d - c) * 50f
            else -> 0f
        }
    }
}

fun calculateOverallIAQ(
    values: List<Float>,
    ranges: List<List<Float>>,
    titles: List<String>
): Int {

    val scores = values.mapIndexed { index, value ->
        calculateIAQPercentage(
            value,
            ranges[index],
            isLowGood(titles[index])
        )
    }

    return scores.average().toInt()
}