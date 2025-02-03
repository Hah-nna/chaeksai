package com.jeong.sesac.sai.model

sealed class UiState<out T> {
    data object Loading : UiState<Nothing>()
    data class Success<T>(val data : T) : UiState<T>()
    data class Error(val error : String) : UiState<Nothing>()
}