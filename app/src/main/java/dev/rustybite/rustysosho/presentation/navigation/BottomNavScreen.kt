package dev.rustybite.rustysosho.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.Navigation
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import dev.rustybite.rustysosho.presentation.home_screen.HomeScreen
import dev.rustybite.rustysosho.presentation.home_screen.HomeViewModel
import dev.rustybite.rustysosho.utils.AppEvents

sealed class BottomNavScreen(val route: String, val name: String, val icon: ImageVector) {
    class Home(name: String) : BottomNavScreen(route = "feeds_screen", name = name, icon = Icons.Default.Home)
    class Charts(name: String) : BottomNavScreen(route = "charts_screen", name = name, icon = Icons.Default.ChatBubble)
    class Profile(name: String) : BottomNavScreen(route = "profile_screen", name = name, icon = Icons.Default.Person)
}

fun NavGraphBuilder.bottomNav(
    home: BottomNavScreen.Home,
    charts: BottomNavScreen.Charts,
    profile: BottomNavScreen.Profile,
    homeScreenOnNavigate: (AppEvents.Navigate) -> Unit,
    homeViewModel: HomeViewModel
) {
    navigation("home", home.route) {
        composable(home.route) {
            HomeScreen(onNavigate = homeScreenOnNavigate, viewModel = homeViewModel)
        }
        composable(charts.route) {

        }
        composable(profile.route) {

        }
    }
}