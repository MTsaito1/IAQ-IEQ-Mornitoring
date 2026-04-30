package com.example.iaqieqmornitoring.utils

import kotlinx.coroutines.*
import kotlin.random.Random
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object SensorRepository {

    private val _data = MutableStateFlow(
        SensorData()
    )
    val data: StateFlow<SensorData> = _data.asStateFlow()

    fun startSimulation() {
        kotlinx.coroutines.GlobalScope.launch {
            while (true) {
                kotlinx.coroutines.delay(2000)

                _data.value = SensorData(
                    temp = Random.nextFloat() * 5 + 20,
                    humidity = Random.nextFloat() * 20 + 40,
                    co2 = Random.nextFloat() * 200 + 400,
                    pm25 = Random.nextFloat() * 50,
                    pm10 = Random.nextFloat() * 80,
                    tvoc = Random.nextFloat() * 80,
                    hcho = Random.nextFloat() * 0.1f,
                    light = Random.nextFloat() * 500,
                    sound = Random.nextFloat() * 80
                )
            }
        }
    }
}

data class SensorData(
    val temp: Float = 22f,
    val humidity: Float = 50f,
    val co2: Float = 400f,
    val pm25: Float = 10f,
    val pm10: Float = 20f,
    val tvoc: Float = 10f,
    val hcho: Float = 0.02f,
    val light: Float = 300f,
    val sound: Float = 40f
)