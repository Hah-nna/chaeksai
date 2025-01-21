package com.jeong.sesac.sai.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jeong.sesac.data.api.manager.KakaoMapManager
import com.jeong.sesac.data.api.manager.OpenAPIManager
import com.jeong.sesac.data.datasource.FireBaseDataSourceImpl
import com.jeong.sesac.data.datasource.KakaoMapDataSourceImpl
import com.jeong.sesac.data.datasource.OpenAPIDataSourceImpl
import com.jeong.sesac.data.repository.KakaoMapRepositoryImpl
import com.jeong.sesac.data.repository.LoginRepositoryImpl
import com.jeong.sesac.data.repository.OpenApiRepositoryImpl
import com.jeong.sesac.sai.viewmodel.KakaoMapViewModel
import com.jeong.sesac.sai.viewmodel.LoginViewModel

@Suppress("UNCHECKED_CAST")
val appViewModelFactory = object : ViewModelProvider.Factory {
    override fun < T : ViewModel>  create(modelClass: Class<T>): T =
        with(modelClass) {
            when {
                isAssignableFrom(LoginViewModel::class.java) ->
                    LoginViewModel(LoginRepositoryImpl(FireBaseDataSourceImpl()))
                isAssignableFrom(KakaoMapViewModel::class.java) -> {
                    val kakaoService = KakaoMapManager.getInstance()
                    KakaoMapViewModel(KakaoMapRepositoryImpl(KakaoMapDataSourceImpl(kakaoService)))
                }
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}