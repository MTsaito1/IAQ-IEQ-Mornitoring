package com.example.iaqieqmornitoring.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import com.example.iaqieqmornitoring.components.*
import com.example.iaqieqmornitoring.utils.IAQConfig
import com.example.iaqieqmornitoring.utils.SensorData
import com.example.iaqieqmornitoring.utils.SensorItem
import com.example.iaqieqmornitoring.utils.SensorRepository

// ==========================
// 🔥 GRAPH GRID (ใช้ค่าจริง)
// ==========================
@Composable
fun AnalyticsGraphGrid(navController: NavController) {

    val sensor by SensorRepository.data.collectAsState()

    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier.fillMaxWidth()
    ) {

        Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
            GraphCard("Temperature", Color.Red, sensor.temp, Icons.Default.Thermostat, navController, Modifier.weight(1f))
            GraphCard("Humidity", Color.Yellow, sensor.humidity, Icons.Default.WaterDrop, navController, Modifier.weight(1f))
            GraphCard("CO2", Color.Yellow, sensor.co2, Icons.Default.Cloud, navController, Modifier.weight(1f))
        }

        Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
            GraphCard("PM2.5", Color.Cyan, sensor.pm25, Icons.Default.Air, navController, Modifier.weight(1f))
            GraphCard("PM10", Color.Green, sensor.pm10, Icons.Default.WindPower, navController, Modifier.weight(1f))
            GraphCard("TVOC", Color.Green, sensor.tvoc, Icons.Default.Science, navController, Modifier.weight(1f))
        }

        Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
            GraphCard("HCHO", Color.Cyan, sensor.hcho, Icons.Default.Biotech, navController, Modifier.weight(1f))
            GraphCard("Light", Color.Red, sensor.light, Icons.Default.LightMode, navController, Modifier.weight(1f))
            GraphCard("Sound", Color(0xFFFF9800), sensor.sound, Icons.Default.GraphicEq, navController, Modifier.weight(1f))
        }
    }
}

// ==========================
// 🔥 GRAPH CARD (ใช้ history)
// ==========================
@Composable
fun GraphCard(
    title: String,
    lineColor: Color,
    value: Float,
    icon: ImageVector,
    navController: NavController,
    modifier: Modifier = Modifier
) {

    val points = remember { mutableStateListOf<Float>() }

    // เก็บ history
    LaunchedEffect(value) {
        if (points.size >= 30) points.removeAt(0)
        points.add(value)
    }

    Card(
        modifier = modifier
            .height(150.dp)
            .clickable {
                navController.navigate("detail/$title")
            },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0B1220))
    ) {

        Box(modifier = Modifier.fillMaxSize()) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(14.dp)
            ) {

                // 🔹 Header
                Row(verticalAlignment = Alignment.CenterVertically) {

                    Icon(
                        icon,
                        contentDescription = title,
                        tint = lineColor,
                        modifier = Modifier.size(18.dp)
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        title,
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // 🔹 Graph
                Canvas(modifier = Modifier.fillMaxSize()) {

                    if (points.size < 2) return@Canvas

                    val max = points.maxOrNull() ?: 1f
                    val min = points.minOrNull() ?: 0f
                    val range = (max - min).takeIf { it != 0f } ?: 1f

                    val stepX = size.width / (points.size - 1)

                    val path = Path()

                    points.forEachIndexed { i, v ->
                        val x = i * stepX

                        val normalized = (v - min) / range

                        val y = size.height - (normalized * size.height * 0.8f + 10f)

                        if (i == 0) path.moveTo(x, y)
                        else path.lineTo(x, y)
                    }

                    // 🔥 glow
                    drawPath(
                        path,
                        lineColor.copy(alpha = 0.15f),
                        style = Stroke(width = 12f, cap = StrokeCap.Round)
                    )

                    // 🔥 main line
                    drawPath(
                        path,
                        lineColor,
                        style = Stroke(width = 4f, cap = StrokeCap.Round)
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // 🔹 Value text
                Text(
                    text = String.format("%.1f", value),
                    color = lineColor,
                    fontSize = 12.sp
                )
            }

            // 🔴 status dot (มุมขวาบน)
            Box(
                modifier = Modifier
                    .padding(10.dp)
                    .size(8.dp)
                    .background(lineColor, CircleShape)
                    .align(Alignment.TopEnd)
            )
        }
    }
}

fun isLowGood(title: String): Boolean {
    return when (title) {
        "CO2", "PM2.5", "PM10", "TVOC", "HCHO" -> true
        else -> false
    }
}
// ==========================
// 🔥 VALUE GRID (แก้แล้ว)
// ==========================
@Composable
fun AnalyticsGrid(
    temp: Float,
    humidity: Float,
    co2: Float,
    pm25: Float,
    pm10: Float,
    tvoc: Float,
    hcho: Float,
    light: Float,
    sound: Float
) {

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            MiniGaugeCard("TEMP", temp, "°C", IAQConfig.getMinMaxRange("TEMP"), Modifier.weight(1f))
            MiniGaugeCard("HUMIDITY", humidity, "%", IAQConfig.getMinMaxRange("HUMIDITY"), Modifier.weight(1f))
            MiniGaugeCard("CO2", co2, "ppm", IAQConfig.getMinMaxRange("CO2"), Modifier.weight(1f))
        }

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            MiniGaugeCard("PM2.5", pm25, "µg/m³", IAQConfig.getMinMaxRange("PM2.5"), Modifier.weight(1f))
            MiniGaugeCard("PM10", pm10, "µg/m³", IAQConfig.getMinMaxRange("PM10"), Modifier.weight(1f))
            MiniGaugeCard("TVOC", tvoc, "mg/m³", IAQConfig.getMinMaxRange("TVOC"), Modifier.weight(1f))
        }

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            MiniGaugeCard("HCHO", hcho, "mg/m³", IAQConfig.getMinMaxRange("HCHO"), Modifier.weight(1f))
            MiniGaugeCard("LIGHT", light, "lux", IAQConfig.getMinMaxRange("LIGHT"), Modifier.weight(1f))
            MiniGaugeCard("SOUND", sound, "dB", IAQConfig.getMinMaxRange("SOUND"), Modifier.weight(1f))
        }
    }
}

// ==========================
// 🔥 MAIN SCREEN
// ==========================
@Composable
fun AnalyticsScreen(nav: NavController) {

    val sensor by SensorRepository.data.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF020617))
    ) {

        TopBar()

        Row(modifier = Modifier.fillMaxSize()) {

            Sidebar(nav)

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(20.dp)
            ) {

                Text(
                    text = "Analytics",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(20.dp))

                AnalyticsGrid(
                    temp = sensor.temp,
                    humidity = sensor.humidity,
                    co2 = sensor.co2,
                    pm25 = sensor.pm25,
                    pm10 = sensor.pm10,
                    tvoc = sensor.tvoc,
                    hcho = sensor.hcho,
                    light = sensor.light,
                    sound = sensor.sound
                )
            }
        }
    }
}

@Composable
fun MiniGaugeCard(
    title: String,
    value: Float,
    unit: String,
    range: ClosedFloatingPointRange<Float>,
    modifier: Modifier = Modifier
) {

    val percent = ((value - range.start) / (range.endInclusive - range.start))
        .coerceIn(0f, 1f)

    val color = when {
        percent < 0.4 -> Color(0xFF00FFA3)
        percent < 0.7 -> Color(0xFFFFEA00)
        else -> Color(0xFFFF1744)
    }

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0B1220))
    ) {

        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Text(title, color = Color.Gray, fontSize = 12.sp)

            // 🔥 MINI GAUGE
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {

                val stroke = 10f

                drawArc(
                    color = Color(0xFF1F2937),
                    startAngle = 180f,
                    sweepAngle = 180f,
                    useCenter = false,
                    style = Stroke(stroke)
                )

                drawArc(
                    color = color,
                    startAngle = 180f,
                    sweepAngle = percent * 180f,
                    useCenter = false,
                    style = Stroke(stroke)
                )
            }

            Text(
                text = "${"%.2f".format(value)} $unit",
                color = Color.White,
                fontSize = 14.sp
            )
        }
    }
}