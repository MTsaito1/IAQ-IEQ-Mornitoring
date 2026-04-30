package com.example.iaqieqmornitoring.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import com.example.iaqieqmornitoring.components.*

@Composable
fun MissionScreen(nav: NavController) {

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        TopBar()

        Row(modifier = Modifier.fillMaxSize()) {

            Sidebar(nav)

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Mission Screen", color = Color.White, fontSize = 24.sp)
            }
        }
    }
}