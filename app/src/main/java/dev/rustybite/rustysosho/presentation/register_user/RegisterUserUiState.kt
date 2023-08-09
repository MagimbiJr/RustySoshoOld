package dev.rustybite.rustysosho.presentation.register_user

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.LocalDateTime


@RequiresApi(Build.VERSION_CODES.O)
data class RegisterUserUiState(
    val loading: Boolean = false,
    val name: String = "",
    val gender: String = "",
    val username: String = "",
    val dateOfBirth: String,
    val uri: Uri? = null,
    val errorMessage: String = "",
)