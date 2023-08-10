package dev.rustybite.rustysosho.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun RSBottomNav(
    navItems: List<BottomNavScreen>,
    navHostController: NavHostController,
    modifier: Modifier
) {
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    var currentRoute = navBackStackEntry?.destination?.route

    BottomAppBar(
        modifier = modifier
    ) {
        navItems.forEach { item ->
            NavigationBarItem(
                selected = item.route == currentRoute,
                onClick = { currentRoute = item.route },
                icon = {
                    Icon(imageVector = item.icon, contentDescription = item.name)
                },
                label = {
                    Text(text = item.name)
                }
            )
        }
    }
}