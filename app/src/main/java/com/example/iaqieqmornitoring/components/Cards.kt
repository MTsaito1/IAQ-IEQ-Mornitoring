package com.example.iaqieqmornitoring.components

import com.example.iaqieqmornitoring.utils.getColorFromLevel
import com.example.iaqieqmornitoring.utils.getLabel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector // ✅ สำคัญมาก
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.iaqieqmornitoring.utils.IAQConfig
import com.example.iaqieqmornitoring.utils.calculateIAQPercentage
import androidx.navigation.NavController
import androidx.compose.foundation.clickable
import com.example.iaqieqmornitoring.utils.getLevelFromScore

// -------------------- ICON --------------------
fun getIcon(title: String): ImageVector = when (title) {
    "MQTT Connection" -> Icons.Default.Wifi
    "Sensor Communication" -> Icons.Default.SettingsInputAntenna
    "Air Quality Sensors" -> Icons.Default.Air
    "Battery Level" -> Icons.Default.BatteryFull
    "Robot Navigation" -> Icons.Default.Navigation
    "Storage System" -> Icons.Default.Storage
    else -> Icons.Default.Info
}

// -------------------- SENSOR CARD --------------------
@Composable
fun SensorCard(
    title: String,
    value: Float,
    unit: String,
    range: List<Float>,
    icon: ImageVector,
    isLowGood: Boolean = false,
    modifier: Modifier = Modifier
) {

    // 🔥 คำนวณ score (ไม่ต้องโชว์)
    val score = calculateIAQPercentage(value, range, isLowGood)

    // 🔥 map เป็น level
    val level = getLevelFromScore(score)

    // 🔥 เอาไปใช้ UI
    val color = getColorFromLevel(level)
    val label = getLabel(level)

    Card(
        modifier = modifier.height(95.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(Color(0xFF0B1220))
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(icon, null, tint = color, modifier = Modifier.size(24.dp))

            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1f)) {

                Text(title, color = Color.Gray, fontSize = 11.sp)

                // ✅ แสดงค่าจริง (นี่แหละที่คุณต้องการ)
                Text(
                    "${"%.2f".format(value)} $unit",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                // ✅ label จาก IAQI
                Text(
                    text = label,
                    color = color,
                    fontSize = 10.sp
                )
            }

            // 🔥 จุดสี
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(color, CircleShape)
            )
        }
    }
}

// -------------------- ANALYTICS CARD --------------------
@Composable
fun AnalyticsCard(
    title: String,
    value: Float,
    unit: String,
    range: List<Float>,
    icon: ImageVector,
    isLowGood: Boolean = false,
    navController: NavController,
    modifier: Modifier = Modifier
) {

    val percent = calculateIAQPercentage(value, range, isLowGood)
    val level = getLevelFromScore(percent)
    val color = getColorFromLevel(level)
    val label = getLabel(level)

    Card(
        modifier = modifier
            .height(140.dp)
            .clickable {
                navController.navigate("detail/$title")
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(Color(0xFF0B1220))
    ) {

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(icon, null, tint = color)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(title, color = Color.White)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "%.2f".format(value),
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(unit, color = Color.Gray, fontSize = 12.sp)
            }

            MiniGauge(
                value = percent,
                label = label
            )
        }
    }
}

    // -------------------- STARTUP CARD --------------------
    @Composable
    fun StartupCard(title: String) {

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0B1220)),
            elevation = CardDefaults.cardElevation(6.dp),
            border = BorderStroke(1.dp, Color(0xFF1E293B))
        ) {

            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Row(verticalAlignment = Alignment.CenterVertically) {

                    Icon(
                        imageVector = getIcon(title),
                        contentDescription = null,
                        tint = Color(0xFF94A3B8),
                        modifier = Modifier.size(35.dp)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(title, color = Color.White, fontSize = 20.sp)
                        Text("Ready", color = Color(0xFF00FFA3), fontSize = 18.sp)
                    }
                }

                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(Color(0xFF00FFA3), CircleShape)
                        .shadow(
                            8.dp,
                            CircleShape,
                            ambientColor = Color(0xFF00FFA3),
                            spotColor = Color(0xFF00FFA3)
                        )
                )
            }
        }
    }


