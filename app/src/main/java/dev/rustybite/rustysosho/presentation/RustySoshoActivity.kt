package dev.rustybite.rustysosho.presentation

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import dev.rustybite.rustysosho.R
import dev.rustybite.rustysosho.di.RustySoshoContainer
import dev.rustybite.rustysosho.presentation.authentication.AuthViewModel
import dev.rustybite.rustysosho.presentation.navigation.RustySoshoNavHost
import dev.rustybite.rustysosho.presentation.register_user.RegisterUserViewModel
import dev.rustybite.rustysosho.presentation.ui.theme.RustySoshoTheme
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class RustySoshoActivity : ComponentActivity() {
    private var shouldShowCamera = mutableStateOf(false)
    private val isPermissionGranted = mutableStateOf(false)
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            shouldShowCamera.value = true
            isPermissionGranted.value = isGranted
        } else {

        }
    }
    private var container: RustySoshoContainer? = null
    private lateinit var authViewModel: AuthViewModel
    private lateinit var userRegViewModel: RegisterUserViewModel
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService

    companion object {
        var rustySoshoActivity: RustySoshoActivity? = null

        fun getInstance(): RustySoshoActivity? = rustySoshoActivity
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rustySoshoActivity = this
        container = RustySoshoContainer()
        authViewModel = AuthViewModel(
            container!!.authRepository,
            container!!.resProvider
        )
        userRegViewModel = RegisterUserViewModel(
            container!!.userRepository,
            container!!.resProvider
        )
        setContent {
            val navHostController = rememberNavController()
            RustySoshoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RustySoshoNavHost(
                        navHostController = navHostController,
                        authViewModel = authViewModel,
                        userRegViewModel = userRegViewModel,
                        shouldShowCamera = shouldShowCamera,
                        isPermissionGranted = isPermissionGranted,
                        requestPermissionLauncher = requestPermissionLauncher,
                        outputDirectory = outputDirectory,
                        executor = cameraExecutor
                    )
                }
            }
        }
        //requestCameraPermission(this, shouldShowCamera, requestPermissionLauncher)
        outputDirectory = getOutputDirectory()
        cameraExecutor = Executors.newSingleThreadExecutor()

    }

    override fun onResume() {
        super.onResume()
        rustySoshoActivity = this
    }

    override fun onRestart() {
        super.onRestart()
        rustySoshoActivity = this
    }

    override fun onDestroy() {
        super.onDestroy()
        rustySoshoActivity = null
        container = null
        cameraExecutor.shutdown()
    }

    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let { file ->
            File(file, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists()) mediaDir else filesDir
    }
}
