package dev.rustybite.rustysosho.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dev.rustybite.rustysosho.presentation.authentication.AuthViewModel
import dev.rustybite.rustysosho.presentation.authentication.SearchCountryCodeScreen
import dev.rustybite.rustysosho.presentation.authentication.SelectCountryCodeScreen
import dev.rustybite.rustysosho.presentation.authentication.EnterNumberScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RustySoshoNavHost(
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
) {
    val systemUiController  = rememberSystemUiController()
    val scrollState = rememberScrollState()

    val uiState = authViewModel.uiState.collectAsState().value
    Scaffold { paddingValues ->
        NavHost(
            navController = navHostController,
            startDestination = "home",
            modifier = modifier
                .padding(paddingValues)
        ) {
            composable(route = "home") {
                EnterNumberScreen(
                    onNavigate = { navHostController.navigate(it.route) },
                    viewModel = authViewModel,
                    systemUiController = systemUiController,
                    scrollState = scrollState
                )
            }
            composable("select_code_screen") {
                SelectCountryCodeScreen(
                    onNavigate = { navHostController.navigate(it.route) },
                    onPopBackClicked = { navHostController.popBackStack() },
                    systemUiController = systemUiController,
                    viewModel = authViewModel
                )
            }
            composable("search_code_screen") {
                SearchCountryCodeScreen(
                    onNavigate = { navHostController.navigate(it.route)},
                    onPopBackClicked = { navHostController.popBackStack() },
                    viewModel = authViewModel,
                    systemUiController = systemUiController
                )
            }

            composable("enter_code_screen") {

                Text(text = "otp is ${uiState.otp}")
            }
        }
    }
}