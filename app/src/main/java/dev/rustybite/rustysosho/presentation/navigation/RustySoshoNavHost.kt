package dev.rustybite.rustysosho.presentation.navigation

import android.os.Build
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.auth.FirebaseAuth
import dev.rustybite.rustysosho.R
import dev.rustybite.rustysosho.presentation.authentication.AuthViewModel
import dev.rustybite.rustysosho.presentation.authentication.SearchCountryCodeScreen
import dev.rustybite.rustysosho.presentation.authentication.SelectCountryCodeScreen
import dev.rustybite.rustysosho.presentation.authentication.EnterNumberScreen
import dev.rustybite.rustysosho.presentation.authentication.VerifyOtpScreen
import dev.rustybite.rustysosho.presentation.home_screen.HomeScreen
import dev.rustybite.rustysosho.presentation.home_screen.HomeViewModel
import dev.rustybite.rustysosho.presentation.register_user.RegisterUserScreen
import dev.rustybite.rustysosho.presentation.register_user.RegisterUserViewModel
import java.io.File
import java.util.concurrent.Executor

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RustySoshoNavHost(
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    outputDirectory: File,
    executor: Executor,
    shouldShowCamera: MutableState<Boolean>,
    isPermissionGranted: MutableState<Boolean>,
    requestPermissionLauncher: ActivityResultLauncher<String>,
    userRegViewModel: RegisterUserViewModel,
    homeViewModel: HomeViewModel
) {
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    var currentRoute = navBackStackEntry?.destination?.route

    val uiState = authViewModel.uiState.collectAsState().value
    val systemUiController = rememberSystemUiController()
    val scrollState = rememberScrollState()
    val auth = FirebaseAuth.getInstance()
    val home = BottomNavScreen.Home(stringResource(id = R.string.home_screen_name))
    val charts = BottomNavScreen.Charts(stringResource(id = R.string.charts_screen_name))
    val profile = BottomNavScreen.Profile(stringResource(id = R.string.profile_screen_name))
    val startDestination = when {
        uiState.userId != null && uiState.isUserStored -> home.route
        !uiState.isUserStored && uiState.userId != null -> "register_user"
        else -> "verify_number_screen"
    }
    var showBottomNav by remember { mutableStateOf(false) }
    val navItems = listOf(
        home,
        charts,
        profile
    )


    Scaffold(
        bottomBar = {
            RSBottomNav(
                navItems = navItems,
                onClick = { route ->
                    navHostController.navigate(route) {
                        popUpTo(navHostController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                currentRoute = currentRoute,
                modifier = modifier
            )
        }
    ) { paddingValues ->
        NavHost(
            navController = navHostController,
            startDestination = startDestination,
            modifier = modifier
                .padding(paddingValues)
        ) {
            composable(home.route) {
                HomeScreen(
                    onNavigate = { navHostController.navigate("add_post_screen") },
                    viewModel = homeViewModel
                )
            }
            composable(charts.route) {}
            composable(profile.route) {}
            composable("register_user") {
                RegisterUserScreen(
                    systemUiController = systemUiController,
                    onNavigate = { navHostController.navigate(it.route) },
                    viewModel = userRegViewModel,
                    scrollState = scrollState,
                    executor = executor,
                    outputDirectory = outputDirectory,
                    shouldShowCamera = shouldShowCamera,
                    isPermissionGranted = isPermissionGranted,
                    requestPermissionLauncher = requestPermissionLauncher
                )
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
            composable("add_post_screen") {
                Text(text = "Add post")
            }
        }
    }
}