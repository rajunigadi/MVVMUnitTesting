package com.raju.mvvm.testing.utils

/*sealed class DataResult<T : Any> {
    class Success<T : Any>(val data: T) : DataResult<T>()
    class Error<T : Any>(val message: String?) : DataResult<T>()
    class Loading<T : Any> : DataResult<T>()
}*/

sealed interface UiState<out T> {
    data class Success<T>(val data: T) : UiState<T>
    data class Error(val message: String) : UiState<Nothing>
    object Loading : UiState<Nothing>
}