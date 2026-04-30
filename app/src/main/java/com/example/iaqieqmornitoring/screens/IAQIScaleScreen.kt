package com.example.iaqieqmornitoring.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import com.example.iaqieqmornitoring.utils.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.ui.draw.shadow
import com.example.iaqieqmornitoring.utils.SensorRepository

@Composable
fun IAQIScaleScreen(nav: NavController) {

    val sensor by SensorRepository.data.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF020817))
    ) {

        Column {

            // 🔹 Top Bar
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            ) {

                Text(
                    text = "←",
                    color = Color.White,
                    fontSize = 22.sp,
                    modifier = Modifier.clickable {
                        nav.popBackStack()
                    }
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = "IAQI Scale Reference",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // 🔥 GRID (FIX ครบ)
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxSize() // ✅ ใช้อันนี้แทน
                    .padding(horizontal = 6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(
                    top = 5.dp,
                    bottom = 80.dp // ✅ กัน nav bar + scroll ได้สุด
                )
            ) {
                item { PollutantCard("TEMP","°C", listOf("<18","18-25","25-30","30-35",">35"), listOf(18f,25f,30f,35f),sensor.temp) }
                item { PollutantCard("HUMID","%", listOf("<30","30-50","50-70","70-85",">85"), listOf(30f,50f,70f,85f), sensor.humidity) }
                item { PollutantCard("CO2","ppm", listOf("<400","400-600","600-1000","1000-2500",">2500"), listOf(400f,600f,1000f,2500f), sensor.co2) }

                item { PollutantCard("PM2.5","µg/m³", listOf("<12","12-35","35-55","55-150",">150"), listOf(12f,35f,55f,150f), 25f) }
                item { PollutantCard("PM10","µg/m³", listOf("<20","20-50","50-100","100-200",">200"), listOf(20f,50f,100f,200f), 78f) }
                item { PollutantCard("TVOC","mg/m³", listOf("<0.3","0.3-0.5","0.5-1.0","1.0-3.0",">3.0"), listOf(0.3f,0.5f,1f,3f), 61f) }

                item { PollutantCard("HCHO","mg/m³", listOf("<0.03","0.03-0.05","0.05-0.10","0.10-0.20",">0.20"), listOf(0.03f,0.05f,0.10f,0.20f), 0f) }
                item { PollutantCard("LIGHT","lux", listOf("<100","100-300","300-500","500-1000",">1000"), listOf(100f,300f,500f,1000f), 399f) }
                item { PollutantCard("SOUND","dB", listOf("<30","30-50","50-70","70-90",">90"), listOf(30f,50f,70f,90f), 0f) }
            }
        }
    }
}

@Composable
fun PollutantCard(
    title: String,
    unit: String,
    rangesText: List<String>,
    rangesValue: List<Float>,
    currentValue: Float
) {

    val score = calculateIAQPercentage(
        currentValue,
        rangesValue,
        isLowGood(title)
    )

    val level = getLevelFromScore(score)

    val animatedValue by animateFloatAsState(
        targetValue = currentValue,
        animationSpec = tween(700)
    )

    val levelLabels = listOf(
        "Good",
        "Moderate",
        "Polluted",
        "Very Polluted",
        "Severe"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .shadow(8.dp, RoundedCornerShape(18.dp)),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF071226)
        ),
        shape = RoundedCornerShape(18.dp)
    ) {

        Column(modifier = Modifier.padding(6.dp)) {

            // 🔹 TITLE
            Text(
                title,
                color = Color.White,
                fontSize = 14.sp
            )

            // 🔹 VALUE (🔥 อัปเกรด)
            Text(
                "${animatedValue.toInt()} $unit",
                color = getColorFromLevel(level),
                fontSize = 25.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(1.dp))

            rangesText.forEachIndexed { i, text ->

                val rowLevel = indexToLevel(i)
                val isActive = rowLevel == level

                val animatedAlpha by animateFloatAsState(
                    targetValue = if (isActive) 1f else 0f,
                    animationSpec = tween(400)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    getColorFromLevel(rowLevel).copy(alpha = 0.5f * animatedAlpha),
                                    Color.Transparent
                                )
                            ),
                            shape = RoundedCornerShape(6.dp)
                        )
                        .padding(vertical = 4.dp, horizontal = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Text(
                        levelLabels[i],
                        color = getColorFromLevel(rowLevel),
                        fontSize = 15.sp,
                        fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium
                    )

                    Text(
                        text,
                        color = Color.White,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}

fun indexToLevel(index: Int): IAQLevel {
    return when (index) {
        0 -> IAQLevel.GOOD
        1 -> IAQLevel.MODERATE
        2 -> IAQLevel.POLLUTED
        3 -> IAQLevel.VERY_POLLUTED
        else -> IAQLevel.SEVERELY_POLLUTED
    }
}