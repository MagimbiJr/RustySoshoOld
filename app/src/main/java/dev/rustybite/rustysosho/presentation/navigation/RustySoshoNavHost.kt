package dev.rustybite.rustysosho.presentation.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RustySoshoNavHost(
    navHostController: NavHostController,
    modifier: Modifier = Modifier
) {
    Scaffold { paddingValues ->
        NavHost(
            navController = navHostController,
            startDestination = "home",
            modifier = modifier
                .padding(paddingValues)
        ) {
            composable(route = "home") {
                Column {
                    Text(text = "Well!.. nothing here. Just setup",)// style = MaterialTheme.typography.headlineLarge
                }
            }
        }
    }
}