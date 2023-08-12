package dev.rustybite.rustysosho.presentation.home_screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import dev.rustybite.rustysosho.R
import dev.rustybite.rustysosho.presentation.ui.components.RustySoshoFab
import dev.rustybite.rustysosho.utils.AppEvents
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigate: (AppEvents.Navigate) -> Unit,
    viewModel: HomeViewModel
) {
    val appEvents = viewModel.appEvents

    LaunchedEffect(key1 = appEvents) {
        appEvents.collectLatest { event ->
            when(event) {
                is AppEvents.Navigate -> onNavigate(event)
                is AppEvents.ShowToast -> Unit
                is AppEvents.ShowSnackBar -> Unit
                is AppEvents.PopBackStack -> Unit
            }
        }
    }
    Scaffold(
        floatingActionButton = {
            RustySoshoFab(
                icon = Icons.Default.Add,
                onClick = viewModel::onFabClick,
                text = stringResource(id = R.string.create),
                expanded = true
            )
        }
    ) { paddingValues ->

    }
}