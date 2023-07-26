package dev.rustybite.rustysosho.utils

sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : Resource<T>(data = data)
    class Failure<T>(message: String) : Resource<T>(message = message)
    object Loading : Resource<Nothing>()
}
