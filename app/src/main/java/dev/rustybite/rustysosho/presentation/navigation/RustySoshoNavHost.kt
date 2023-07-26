package dev.rustybite.rustysosho.presentation.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.auth.FirebaseAuth
import dev.rustybite.rustysosho.presentation.authentication.AuthViewModel
import dev.rustybite.rustysosho.presentation.authentication.SearchCountryCodeScreen
import dev.rustybite.rustysosho.presentation.authentication.SelectCountryCodeScreen
import dev.rustybite.rustysosho.presentation.authentication.EnterNumberScreen
import dev.rustybite.rustysosho.presentation.authentication.VerifyOtpScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RustySoshoNavHost(
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
) {
    val systemUiController = rememberSystemUiController()
    val scrollState = rememberScrollState()
    val auth = FirebaseAuth.getInstance()

    val uiState = authViewModel.uiState.collectAsState().value
    Scaffold { paddingValues ->
        NavHost(
            navController = navHostController,
            startDestination = if (auth.currentUser != null) "home" else "verify_number_screen",
            modifier = modifier
                .padding(paddingValues)
        ) {
            composable("home") {
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "Welcome home user")
                }
            }

            composable(route = "verify_number_screen") {
                EnterNumberScreen(
                    onNavigate = {
                        navHostController.navigate(it.route) {
                            popUpTo(navHostController.graph.findStartDestination().id)
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    viewModel = authViewModel,
                    systemUiController = systemUiController,
                    scrollState = scrollState
                )
            }
            composable("select_code_screen") {
                SelectCountryCodeScreen(
                    onNavigate = {
                        navHostController.navigate(it.route) {
                            popUpTo(navHostController.graph.findStartDestination().id)
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onPopBackClicked = { navHostController.popBackStack() },
                    systemUiController = systemUiController,
                    viewModel = authViewModel
                )
            }
            composable("search_code_screen") {
                SearchCountryCodeScreen(
                    onNavigate = {
                        navHostController.navigate(it.route) {
                            popUpTo(navHostController.graph.findStartDestination().id)
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onPopBackClicked = { navHostController.popBackStack() },
                    viewModel = authViewModel,
                    systemUiController = systemUiController
                )
            }
            composable("enter_code_screen") {
                VerifyOtpScreen(
                    onNavigate = {
                        navHostController.navigate(it.route) {
                            popUpTo(navHostController.graph.findStartDestination().id)
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    viewModel = authViewModel,
                    systemUiController = systemUiController,
                    scrollState = scrollState
                )
            }
        }
    }
}