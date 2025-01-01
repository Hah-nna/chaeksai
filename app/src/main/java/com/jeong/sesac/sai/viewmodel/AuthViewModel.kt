package com.jeong.sesac.sai.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeong.sesac.datamodule.repository.AuthRepositoryImpl
import com.jeong.sesac.sai.viewmodel.entity.LoginState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class AuthViewModel(private val authRepo: AuthRepositoryImpl) : ViewModel() {
    private var _loginState = MutableStateFlow<LoginState<String>>(LoginState.Loading)
    val loginState get() = _loginState.asStateFlow()

    fun checkLoginState() {
        viewModelScope.launch {
            authRepo.getUserInfo().collectLatest { nickname ->
                _loginState.value = when {
                    nickname.isEmpty() -> LoginState.NotLoggedIn
                    else -> LoginState.Success(nickname)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setUserInfo(nickname: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            try {
                authRepo.createUser(nickname)
                authRepo.setUserInfo(nickname)
                _loginState.value = LoginState.Success(nickname)
            } catch (e: Exception) {
                _loginState.value = LoginState.Error(e.message ?: "오류 발생")
            }
        }
    }



}