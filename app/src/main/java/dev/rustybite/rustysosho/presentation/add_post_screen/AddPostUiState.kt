package dev.rustybite.rustysosho.presentation.add_post_screen

import android.net.Uri
import dev.rustybite.rustysosho.domain.model.User

data class AddPostUiState(
    val loading: Boolean = false,
    val postCaption: String = "",
    val uri: Uri? = null,
    val user: User? = null,
    val errorMessage: String = "",
    val privacyStatus: String = ""
)
