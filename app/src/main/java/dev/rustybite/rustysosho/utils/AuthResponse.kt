package dev.rustybite.rustysosho.utils

sealed class AuthResponse {
    object NotInitialize : AuthResponse()
    class Loading(val message: String?) : AuthResponse()
    class Success(val message: String) : AuthResponse()
    class Failure(val exception: Throwable?) : AuthResponse()
}
