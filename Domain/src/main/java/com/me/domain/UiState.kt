package com.me.domain

sealed interface UiState<out T> {
    data class Loading<T>(val data: T? = null) : UiState<T>
    data class Success<T>(val data: T) : UiState<T>
    data class Error<T>(val message: String, val data: T? = null) : UiState<T>
}