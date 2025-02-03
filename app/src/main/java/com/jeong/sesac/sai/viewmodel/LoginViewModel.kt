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
    private var _duplicateState = MutableStateFlow<UiState<String>>(UiState.Loading)
    val duplicateState get() = _duplicateState.asStateFlow()

    private val _userCreateState = MutableStateFlow<UiState<String>>(UiState.Loading)
    val userCreateState = _userCreateState.asStateFlow()

    @SuppressLint("NewApi")
    fun serUserInfo(nickname: String) = viewModelScope.launch {
        _userCreateState.value = UiState.Loading
        val isSuccess = loginRepo.setUser(nickname)
        _userCreateState.value =
            if (isSuccess) UiState.Success(nickname) else UiState.Error("다시 시도해주세요")
    }

    fun checkDuplicatedNickname(nickname: String) = viewModelScope.launch {
        _duplicateState.value = UiState.Loading
        val isSuccess = loginRepo.checkDuplicateNickname(nickname)
        _duplicateState.value =
            if (isSuccess) UiState.Success(nickname) else UiState.Error("다시 시도해주세요")
    }

}

