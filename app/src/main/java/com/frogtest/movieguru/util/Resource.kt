package com.frogtest.movieguru.util

sealed class Resource<T>(
    val data: T? = null,
    val errorMessage: String? = null,
    val isLoading: Boolean = false
) {
    class Success<T>(data: T?) : Resource<T>(data = data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data = data, errorMessage = message)
    class Loading<T>(isLoading: Boolean = true) : Resource<T>(data = null, isLoading = isLoading)
}
