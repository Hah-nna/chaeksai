package com.jeong.sesac.sai.viewmodel.entity

sealed class LoginState<out T> {
    data object Loading : LoginState<Nothing>()
    data object NotLoggedIn : LoginState<Nothing>()
    data class Success<out T>(val data : T) : LoginState<T>()
    data class Error(val error : String) : LoginState<Nothing>()
}