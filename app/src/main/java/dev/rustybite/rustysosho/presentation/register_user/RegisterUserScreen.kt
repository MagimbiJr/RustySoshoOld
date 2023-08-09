package dev.rustybite.rustysosho.presentation.register_user

import android.os.Build
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import com.google.accompanist.systemuicontroller.SystemUiController
import dev.rustybite.rustysosho.utils.AppEvents
import kotlinx.coroutines.flow.collectLatest
import java.io.File
import java.util.concurrent.Executor

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterUserScreen(
    systemUiController: SystemUiController,
    onNavigate: (AppEvents.Navigate) -> Unit,
    viewModel: RegisterUserViewModel,
    scrollState: ScrollState,
    modifier: Modifier = Modifier,
    shouldShowCamera: MutableState<Boolean>,
    isPermissionGranted: MutableState<Boolean>,
    requestPermissionLauncher: ActivityResultLauncher<String>,
    outputDirectory: File,
    executor: Executor
) {
    systemUiController.setSystemBarsColor(MaterialTheme.colorScheme.background)
    val uiState = viewModel.uiState.collectAsState().value
    val focusManager = LocalFocusManager.current
    val appEvents = viewModel.appEvents
    val context = LocalContext.current


    LaunchedEffect(key1 = appEvents) {
        appEvents.collectLatest { event ->
            when(event) {
                is AppEvents.Navigate -> onNavigate(event)
                is AppEvents.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                }
                is AppEvents.ShowSnackBar -> Unit
                is AppEvents.PopBackStack -> Unit
            }
        }
    }

    Scaffold { paddingValues ->
        RegisterUserContent(
            uiState = uiState,
            onNameChange = viewModel::onNameChange,
            onGenderChange = viewModel::onGenderChange,
            onUserNameChange = viewModel::onUsernameChange,
            onDateOfBirthChange = viewModel::onDateOfBirthChange,
            onUriChange = viewModel::onUriChange,
            onCreateClick = {
                viewModel.registerUser(
                    name = uiState.name,
                    username = uiState.username,
                    birthDate = uiState.dateOfBirth,
                    gender = uiState.gender,
                    uri = uiState.uri
                )
            },
            modifier = modifier
                .padding(paddingValues),
            scrollState = scrollState,
            focusManager = focusManager,
            shouldShowCamera = shouldShowCamera,
            isPermissionGranted = isPermissionGranted,
            requestPermissionLauncher = requestPermissionLauncher,
            outputDirectory = outputDirectory,
            executor = executor
        )
    }
}