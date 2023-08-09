package dev.rustybite.rustysosho.presentation.ui_utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.MutableState
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import dev.rustybite.rustysosho.presentation.RustySoshoActivity

fun requestCameraPermission(
    context: Context,
    shouldShowCamera: MutableState<Boolean>,
    requestPermissionLauncher: ActivityResultLauncher<String>
) {
    when {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED -> {
            shouldShowCamera.value = true
        }
        ActivityCompat.shouldShowRequestPermissionRationale(
            context as RustySoshoActivity,
            Manifest.permission.CAMERA
        ) -> {}
        else -> requestPermissionLauncher.launch(Manifest.permission.CAMERA)
    }
}