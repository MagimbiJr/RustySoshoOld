package dev.rustybite.rustysosho.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavScreen(val route: String, val name: String, val icon: ImageVector) {
    class Home(name: String) : BottomNavScreen(route = "feeds_screen", name = name, icon = Icons.Default.Home)
    class Charts(name: String) : BottomNavScreen(route = "charts_screen", name = name, icon = Icons.Default.ChatBubble)
    class Profile(name: String) : BottomNavScreen(route = "profile_screen", name = name, icon = Icons.Default.Person)
}