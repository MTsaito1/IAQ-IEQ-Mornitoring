package com.example.iaqieqmornitoring.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import com.example.iaqieqmornitoring.components.MiniGauge
import com.example.iaqieqmornitoring.utils.SensorRepository

@Composable
fun DetailScreen(
    sensorName: String,
    navController: NavController
) {

    val sensor by SensorRepository.data.collectAsState()

    val value = when (sensorName) {
        "CO2" -> sensor.co2
        "PM2.5" -> sensor.pm25
        "PM10" -> sensor.pm10
        "TEMP" -> sensor.temp
        "HUMID" -> sensor.humidity
        "SOUND" -> sensor.sound
        "TVOC" -> sensor.tvoc
        "HCHO" -> sensor.hcho
        "LIGHT" -> sensor.light
        else -> 0f
    }

    val history = remember { mutableStateListOf<Float>() }

    LaunchedEffect(value) {
        if (history.size >= 60) history.removeAt(0)
        history.add(value)
    }

    val min = history.minOrNull() ?: value
    val max = history.maxOrNull() ?: value
    val avg = history.average().toFloat()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF020817))
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {

        Text(
            "← Back",
            color = Color.Gray,
            modifier = Modifier.clickable { navController.popBackStack() }
        )

        Spacer(Modifier.height(10.dp))

        Text(sensorName, color = Color.White, fontSize = 26.sp)

        Text(
            String.format("%.2f", value),
            color = Color.Cyan,
            fontSize = 36.sp
        )

        Spacer(Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            Box(modifier = Modifier.weight(1f)) {
                CardBox {
                    MiniGauge(value = value, label = sensorName)
                }
            }

            Box(modifier = Modifier.weight(1f)) {
                CardBox {
                    Column {
                        StatRow("Current", value)
                        StatRow("Min", min)
                        StatRow("Max", max)
                        StatRow("Avg", avg)
                    }
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        CardBox {
            AdvancedGraph(history)
        }
    }
}

//////////////////////////////////////////////////////////

@Composable
fun CardBox(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0B1220))
    ) {
        Box(Modifier.padding(16.dp)) {
            content()
        }
    }
}

@Composable
fun StatRow(title: String, value: Float) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(title, color = Color.Gray)
        Text("%.2f".format(value), color = Color.White)
    }
}

//////////////////////////////////////////////////////////
// 🔥 Graph
//////////////////////////////////////////////////////////

@Composable
fun AdvancedGraph(points: List<Float>) {

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
    ) {

        if (points.size < 2) return@Canvas

        val max = points.maxOrNull() ?: 1f
        val min = points.minOrNull() ?: 0f
        val range = (max - min).takeIf { it != 0f } ?: 1f

        val stepX = size.width / (points.size - 1)

        val path = Path()

        points.forEachIndexed { i, v ->

            val x = i * stepX
            val y = size.height - ((v - min) / range * size.height * 0.9f)

            if (i == 0) path.moveTo(x, y)
            else path.lineTo(x, y)
        }

        drawPath(
            path,
            Color.Cyan.copy(alpha = 0.2f),
            style = Stroke(width = 10f)
        )

        drawPath(
            path,
            Color.Cyan,
            style = Stroke(width = 4f)
        )
    }
}