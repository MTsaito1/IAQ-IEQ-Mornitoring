package com.example.iaqieqmornitoring.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun Sidebar(nav: NavController) {

    // 🔥 ทำให้ active menu update อัตโนมัติ
    val backStackEntry by nav.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route ?: ""

    Column(
        modifier = Modifier
            .width(220.dp)
            .fillMaxHeight()
            .background(Color(0xFF020617))
            .padding(vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        SidebarItem("home", "Home", Icons.Default.Home, currentRoute, nav)
        SidebarItem("analytics", "Analytics", Icons.Default.BarChart, currentRoute, nav)
        SidebarItem("mission", "Mission", Icons.Default.Navigation, currentRoute, nav)
        SidebarItem("settings", "Settings", Icons.Default.Settings, currentRoute, nav)
    }
}

@Composable
fun SidebarItem(
    route: String,
    title: String,
    icon: ImageVector,
    current: String,
    nav: NavController
) {

    val isActive = route == current

    val bgBrush = if (isActive) {
        Brush.horizontalGradient(
            listOf(
                Color(0xFF2563EB),
                Color(0xFF1D4ED8)
            )
        )
    } else {
        Brush.horizontalGradient(
            listOf(Color.Transparent, Color.Transparent)
        )
    }

    val textColor = if (isActive) Color.White else Color(0xFF94A3B8)

    Row(
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .shadow(
                elevation = if (isActive) 10.dp else 0.dp,
                shape = RoundedCornerShape(14.dp),
                ambientColor = Color(0xFF2563EB),
                spotColor = Color(0xFF2563EB)
            )
            .clip(RoundedCornerShape(14.dp))
            .background(bgBrush)
            .clickable {

                // 🔥 FIX หลัก: navigation ไม่ค้างหน้าเดิมแล้ว
                if (current != route) {
                    nav.navigate(route) {
                        popUpTo("home")   // ใช้ home เป็น root
                        launchSingleTop = true
                    }
                }
            }
            .padding(vertical = 14.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = textColor,
            modifier = Modifier.size(22.dp)
        )

        Spacer(modifier = Modifier.width(14.dp))

        Text(
            text = title,
            color = textColor,
            fontSize = 16.sp,
            fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}