package com.jeong.sesac.sai.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeong.sesac.datamodule.repository.LoginRepositoryImpl
import com.jeong.sesac.sai.viewmodel.entity.UiState
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
        runCatching {
            loginRepo.setUser(nickname)
        }.onSuccess {
            _userCreateState.value = UiState.Success(nickname)
        }.onFailure {
            _userCreateState.value = UiState.Error("유저 생성실패")
        }.getOrThrow()
    }

    fun checkDuplicatedNickname(nickname: String) = viewModelScope.launch {
        _duplicateState.value = UiState.Loading
        runCatching {
            loginRepo.checkDuplicateNickname(nickname)
        }.onSuccess {
            _duplicateState.value = UiState.Error("닉네임 중복")
        }.onFailure {
            _duplicateState.value = UiState.Success(nickname)
        }.getOrThrow()
    }


}

