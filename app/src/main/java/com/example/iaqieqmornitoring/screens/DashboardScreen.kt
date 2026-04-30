package com.example.iaqieqmornitoring.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import com.example.iaqieqmornitoring.components.*
import com.example.iaqieqmornitoring.utils.*

@Composable
fun DashboardScreen(nav: NavController) {

    val sensor by SensorRepository.data.collectAsState()

    val temp = sensor.temp
    val humidity = sensor.humidity
    val co2 = sensor.co2
    val pm25 = sensor.pm25
    val pm10 = sensor.pm10
    val tvoc = sensor.tvoc
    val hcho = sensor.hcho
    val light = sensor.light
    val sound = sensor.sound

    // 🔥 IAQ CALC
    val tempScore = calculateIAQPercentage(temp, IAQConfig.tempRange, isLowGood("TEMP"))
    val humidityScore = calculateIAQPercentage(humidity, IAQConfig.humidityRange, isLowGood("HUMIDITY"))
    val co2Score = calculateIAQPercentage(co2, IAQConfig.co2Range, isLowGood("CO2"))
    val pm25Score = calculateIAQPercentage(pm25, IAQConfig.pm25Range, isLowGood("PM2.5"))
    val pm10Score = calculateIAQPercentage(pm10, IAQConfig.pm10Range, isLowGood("PM10"))
    val tvocScore = calculateIAQPercentage(tvoc, IAQConfig.tvocRange, isLowGood("TVOC"))
    val hchoScore = calculateIAQPercentage(hcho, IAQConfig.hchoRange, isLowGood("HCHO"))
    val lightScore = calculateIAQPercentage(light, IAQConfig.lightRange, isLowGood("LIGHT"))
    val soundScore = calculateIAQPercentage(sound, IAQConfig.soundRange, isLowGood("SOUND"))

    val iaq = listOf(
        tempScore,
        humidityScore,
        co2Score,
        pm25Score,
        pm10Score,
        tvocScore,
        hchoScore,
        lightScore,
        soundScore
    ).average().toInt()

    val ieq = ((temp + humidity) / 2).toInt()

    val level = when {
        iaq >= 81 -> 0
        iaq >= 61 -> 1
        iaq >= 41 -> 2
        iaq >= 21 -> 3
        else -> 4
    }

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

                // =========================
                // 🔥 TOP SECTION (FIXED)
                // =========================
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Card(
                        modifier = Modifier
                            .weight(2f)
                            .height(280.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(Color(0xFF0B1220))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {

                            IAQGauge(
                                iaqValue = iaq,
                                ieqValue = ieq,
                                level = level
                            )

                            Spacer(modifier = Modifier.width(20.dp))

                            IAQLegend()
                        }
                    }

                    Spacer(modifier = Modifier.width(20.dp))

                    // 🔥 FIX: ใส่ weight
                    MissionPanel(
                        modifier = Modifier
                            .weight(1f)
                            .height(280.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // =========================
                // 🔥 GRID SECTION (FIXED)
                // =========================
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)   // 🔥 สำคัญมาก
                ) {
                    SensorGrid(
                        temp = temp,
                        humidity = humidity,
                        co2 = co2,
                        pm25 = pm25,
                        pm10 = pm10,
                        tvoc = tvoc,
                        hcho = hcho,
                        light = light,
                        sound = sound
                    )
                }
            }
        }
    }
}