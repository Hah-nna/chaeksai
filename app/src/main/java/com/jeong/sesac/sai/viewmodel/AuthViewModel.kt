package com.jeong.sesac.sai.viewmodel

import androidx.lifecycle.ViewModel
import com.jeong.sesac.sai.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthViewModel(private val authRepo : AuthRepository) : ViewModel() {
    private var _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn get() = _isLoggedIn.asStateFlow()

    fun login() {
        // authRepo.login() 호출해서 성공하면
        // _isLoggedIn = true
        // 프래그먼트에서 viewModel.isLoggedIn.collectLatest() 해서 상태값 받아오기
    }

    fun signUp() {
        // authRepo.signUp() 호출해서 성공하면
        // _isLoggedIn = true
        // 프래그먼트에서 viewModel.isLoggedIn.collectLatest() 해서 상태값 받아오기
        // 프래그먼트에서 private val viewModes by activityViewModels<AuthViewModel>() <- 이런식으로 뷰모델객체를 얻어서 ㄱ
    }

}