package com.jeong.sesac.sai.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeong.sesac.data.repository.LoginRepositoryImpl
import com.jeong.sesac.sai.model.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val loginRepo: LoginRepositoryImpl) : ViewModel() {
    private var _duplicateState = MutableStateFlow<UiState<Boolean>>(UiState.Loading)
    val duplicateState get() = _duplicateState.asStateFlow()

    private val _userCreateState = MutableStateFlow<UiState<String>>(UiState.Loading)
    val userCreateState = _userCreateState.asStateFlow()

    @SuppressLint("NewApi")
    fun setUserInfo(nickname: String) = viewModelScope.launch {
        _userCreateState.value = UiState.Loading
        loginRepo.setUser(nickname)
            .onSuccess {
                _userCreateState.value = UiState.Success(it)
            }.onFailure {
                UiState.Error("다시 시도해주세요")
            }
    }

    fun checkDuplicatedNickname(nickname: String) = viewModelScope.launch {
        _duplicateState.value = UiState.Loading
        loginRepo.checkDuplicateNickname(nickname)
            .onSuccess {
                _duplicateState.value = UiState.Success(it)
            }.onFailure {
                _duplicateState.value = UiState.Error("다시 시도해주세요")
            }
    }
}

