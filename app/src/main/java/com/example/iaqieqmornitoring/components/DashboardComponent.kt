package com.example.iaqieqmornitoring.components

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.foundation.clickable
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.Alignment
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Biotech
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WindPower
import androidx.compose.material3.Text
import com.example.iaqieqmornitoring.utils.IAQConfig

@Composable
fun MissionPanel(
    modifier: Modifier = Modifier,
    onStartMission: () -> Unit = {}
) {

    var selectedRoute by remember { mutableStateOf("Route A") }
    var isRunning by remember { mutableStateOf(false) }

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0B1220))
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Column {

                Text(
                    "Mission Control",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(14.dp))

                // 🔽 ROUTE SELECT (UI แบบ button)
                Text("Inspection Route", color = Color.Gray, fontSize = 13.sp)

                Spacer(modifier = Modifier.height(6.dp))

                var expanded by remember { mutableStateOf(false) }
                val routes = listOf("Route A", "Route B", "Route C")

                Box {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF111827), RoundedCornerShape(12.dp))
                            .clickable { expanded = true }
                            .padding(12.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(selectedRoute, color = Color.White)

                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null,
                                tint = Color.Gray
                            )
                        }
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.background(Color(0xFF0B1220))
                    ) {
                        routes.forEach { route ->
                            DropdownMenuItem(
                                text = {
                                    Text(route, color = Color.White)
                                },
                                onClick = {
                                    selectedRoute = route
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    "3 locations • ~15 min",
                    color = Color(0xFF94A3B8),
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 🔥 STATUS แบบมี dot
                Row(verticalAlignment = Alignment.CenterVertically) {

                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(
                                if (isRunning) Color(0xFF00FFA3) else Color.Gray,
                                CircleShape
                            )
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = if (isRunning) "Running" else "Idle",
                        color = if (isRunning) Color(0xFF00FFA3) else Color.Gray,
                        fontSize = 14.sp
                    )
                }
            }

            // 🔥 BUTTON แบบดูเป็น action จริง
            Button(
                onClick = {
                    isRunning = !isRunning
                    onStartMission()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isRunning)
                        Color(0xFFEF4444)
                    else
                        Color(0xFF10B981)
                )
            ) {

                Text(
                    text = if (isRunning) "Stop Mission" else "Start Mission",
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun SensorGrid(
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

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {

        // 🔹 ROW 1
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {

            SensorCard(
                title = "TEMP",
                value = temp,
                unit = "°C",
                range = IAQConfig.tempRange,
                icon = Icons.Default.Thermostat,
                modifier = Modifier.weight(1f)
            )

            SensorCard(
                title = "HUMIDITY",
                value = humidity,
                unit = "%",
                range = IAQConfig.humidityRange,
                icon = Icons.Default.WaterDrop,
                modifier = Modifier.weight(1f)
            )

            SensorCard(
                title = "CO2",
                value = co2,
                unit = "ppm",
                range = IAQConfig.co2Range,
                icon = Icons.Default.Cloud,
                isLowGood = true, // 🔥 สำคัญ
                modifier = Modifier.weight(1f)
            )
        }

        // 🔹 ROW 2
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {

            SensorCard(
                title = "PM2.5",
                value = pm25,
                unit = "µg/m³",
                range = IAQConfig.pm25Range,
                icon = Icons.Default.Air,
                isLowGood = true,
                modifier = Modifier.weight(1f)
            )

            SensorCard(
                title = "PM10",
                value = pm10,
                unit = "µg/m³",
                range = IAQConfig.pm10Range,
                icon = Icons.Default.WindPower,
                isLowGood = true,
                modifier = Modifier.weight(1f)
            )

            SensorCard(
                title = "TVOC",
                value = tvoc,
                unit = "mg/m³",
                range = IAQConfig.tvocRange,
                icon = Icons.Default.Science,
                isLowGood = true,
                modifier = Modifier.weight(1f)
            )
        }

        // 🔹 ROW 3
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {

            SensorCard(
                title = "HCHO",
                value = hcho,
                unit = "mg/m³",
                range = IAQConfig.hchoRange,
                icon = Icons.Default.Biotech,
                isLowGood = true,
                modifier = Modifier.weight(1f)
            )

            SensorCard(
                title = "LIGHT",
                value = light,
                unit = "lux",
                range = IAQConfig.lightRange,
                icon = Icons.Default.LightMode,
                isLowGood = true,
                modifier = Modifier.weight(1f)
            )

            SensorCard(
                title = "SOUND",
                value = sound,
                unit = "dB",
                range = IAQConfig.soundRange,
                icon = Icons.Default.GraphicEq,
                isLowGood = true,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun StatusDot(title: String, isActive: Boolean) {

    val color = if (isActive) Color(0xFF00FFA3) else Color.Red

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .size(8.dp)
                .background(color, CircleShape)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = title,
            color = Color.Gray,
            fontSize = 10.sp
        )
    }
}