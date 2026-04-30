package com.example.iaqieqmornitoring.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import com.example.iaqieqmornitoring.components.StartupCard
import com.example.iaqieqmornitoring.components.TopBar

@Composable
fun StartupScreen(nav: NavController) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF020617),
                        Color(0xFF020617),
                        Color(0xFF0F172A),
                        Color(0xFF020617)
                    )
                )
            )
    ) {

        Column {

            // 🔥 TOPBAR (เต็มจอ)
            TopBar()

            // 🔽 CONTENT (มี padding เท่านั้น)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {

                Spacer(modifier = Modifier.height(8.dp))

                // 🔥 TITLE
                Text(
                    text = "System Startup Check",
                    color = Color.White,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(24.dp))

                // 🔲 GRID
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.height(260.dp)
                ) {
                    items(
                        listOf(
                            "MQTT Connection",
                            "Sensor Communication",
                            "Air Quality Sensors",
                            "Battery Level",
                            "Robot Navigation",
                            "Storage System"
                        )
                    ) { title ->
                        StartupCard(title)
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // 🔥 STATUS BOX
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF020617)
                    ),
                    elevation = CardDefaults.cardElevation(12.dp)
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = "System Status",
                            color = Color.Gray,
                            fontSize = 25.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // 🔥 READY + GLOW
                        Text(
                            text = "READY",
                            style = TextStyle(
                                color = Color(0xFF00FFA3),
                                fontSize = 40.sp,
                                fontWeight = FontWeight.Bold,
                                shadow = Shadow(
                                    color = Color(0xFF00FFA3),
                                    blurRadius = 25f
                                )
                            )
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // 🔘 BUTTON
                        Button(
                            onClick = { nav.navigate("home") },
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF22C1DC)
                            ),
                            modifier = Modifier
                                .height(60.dp)
                                .width(270.dp)
                        ) {
                            Text(
                                text = "Enter Dashboard",
                                color = Color.White,
                                fontSize = 30.sp
                            )
                        }
                    }
                }
            }
        }
    }
}