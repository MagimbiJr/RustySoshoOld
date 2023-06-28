package dev.rustybite.rustysosho.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import dev.rustybite.rustysosho.presentation.navigation.RustySoshoNavHost
import dev.rustybite.rustysosho.presentation.ui.theme.RustySoshoTheme

class RustySoshoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navHostController = rememberNavController()
            RustySoshoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RustySoshoNavHost(navHostController = navHostController)
                }
            }
        }
    }
}
