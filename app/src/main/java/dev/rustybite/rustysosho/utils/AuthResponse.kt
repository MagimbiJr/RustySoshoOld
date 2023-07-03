package dev.rustybite.rustysosho.utils

sealed class AuthResponse {
    object NotInitialize : AuthResponse()
    class Loading(val loading: Boolean = false) : AuthResponse()
    class Success(val message: String) : AuthResponse()
    class Failure(val exception: Throwable?) : AuthResponse()
}
