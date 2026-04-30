package com.example.iaqieqmornitoring.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.iaqieqmornitoring.screens.*

@Composable
fun AppNavigation() {

    val nav = rememberNavController()

    NavHost(
        navController = nav,
        startDestination = "startup"
    ) {
        composable("iaqi") {
            IAQIScaleScreen(nav)
        }

        composable("startup") {
            StartupScreen(nav)
        }

        composable("home") {
            DashboardScreen(nav)
        }

        composable("analytics") {
            AnalyticsScreen(nav)
        }

        composable("mission") {
            MissionScreen(nav)
        }

        composable("settings") {
            SettingsScreen(nav)
        }

        composable(
            route = "detail/{sensor}",
            arguments = listOf(navArgument("sensor") { type = NavType.StringType })
        ) { backStackEntry ->

            val sensorName = backStackEntry.arguments?.getString("sensor") ?: ""

            DetailScreen(sensorName = sensorName, navController = nav)
        }
    }
}