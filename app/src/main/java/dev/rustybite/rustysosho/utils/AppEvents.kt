package dev.rustybite.rustysosho.utils

sealed class AppEvents {
    data class Navigate(val route: String) : AppEvents()
    data class ShowSnackBar(val message: String) : AppEvents()
    data class ShowToast(val message: String) : AppEvents()
    object PopBackStack : AppEvents()
}
