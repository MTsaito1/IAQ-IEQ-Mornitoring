package com.example.iaqieqmornitoring.utils

import androidx.compose.ui.graphics.Color

object IAQConfig {

    // =========================
    // 🌡️ BREAKPOINT TABLE (ของเดิมคุณ ✅)
    // =========================

    val tempRange = listOf(18f, 25f, 30f, 35f)
    val humidityRange = listOf(30f, 50f, 70f, 85f)
    val co2Range = listOf(400f, 600f, 1000f, 2500f)
    val pm25Range = listOf(12f, 35f, 55f, 150f)
    val pm10Range = listOf(20f, 50f, 100f, 200f)
    val tvocRange = listOf(0.3f, 0.5f, 1.0f, 3.0f)
    val hchoRange = listOf(0.03f, 0.05f, 0.1f, 0.2f)
    val lightRange = listOf(100f, 300f, 500f, 1000f)
    val soundRange = listOf(30f, 50f, 70f, 90f)

    // =========================
    // 🎯 RANGE BY TITLE (ของเดิมคุณ ✅)
    // =========================

    fun getRange(title: String): List<Float> {
        return when (title) {
            "TEMP" -> tempRange
            "HUMIDITY" -> humidityRange
            "CO2" -> co2Range
            "PM2.5" -> pm25Range
            "PM10" -> pm10Range
            "TVOC" -> tvocRange
            "HCHO" -> hchoRange
            "LIGHT" -> lightRange
            "SOUND" -> soundRange
            else -> listOf(0f, 25f, 50f, 75f)
        }
    }

    // =========================
    // 🔥 เพิ่มตัวนี้ (สำคัญมาก)
    // =========================
    fun getMinMaxRange(title: String): ClosedFloatingPointRange<Float> {
        val range = getRange(title)
        return range.first()..range.last()
    }
}

// =========================
// 🎯 IAQ LEVEL (ของเดิมคุณ ✅)
// =========================

fun getIAQLevel(value: Float, range: List<Float>): Int {
    return when {
        value <= range[0] -> 0   // Good
        value <= range[1] -> 1   // Moderate
        value <= range[2] -> 2   // Polluted
        value <= range[3] -> 3   // Very Polluted
        else -> 4                // Severe
    }
}