package com.example.iaqieqmornitoring

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.iaqieqmornitoring.navigation.AppNavigation
import com.example.iaqieqmornitoring.ui.theme.IAQIEQMornitoringTheme
import com.example.iaqieqmornitoring.utils.SensorRepository

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        SensorRepository.startSimulation()

        enableEdgeToEdge()

        setContent {
            IAQIEQMornitoringTheme {
                AppNavigation()
            }
        }
    }
}