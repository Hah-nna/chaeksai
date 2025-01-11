package com.jeong.sesac.sai.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jeong.sesac.datamodule.api.manager.KakaoMapManager
import com.jeong.sesac.datamodule.api.manager.OpenAPIManager
import com.jeong.sesac.datamodule.api.service.KakaoMapService
import com.jeong.sesac.datamodule.datasource.FireBaseDataSourceImpl
import com.jeong.sesac.datamodule.datasource.KakaoMapDataSourceImpl
import com.jeong.sesac.datamodule.datasource.OpenAPIDataSourceImpl
import com.jeong.sesac.datamodule.repository.KakaoMapRepositoryImpl
import com.jeong.sesac.datamodule.repository.LoginRepositoryImpl
import com.jeong.sesac.datamodule.repository.OpenApiRepositoryImpl
import com.jeong.sesac.sai.viewmodel.KakaoMapViewModel
import com.jeong.sesac.sai.viewmodel.LoginViewModel
import com.jeong.sesac.sai.viewmodel.RegisterNoteViewModel

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
                isAssignableFrom(RegisterNoteViewModel::class.java) -> {
                    val OpenApiService = OpenAPIManager.getInstance()
                    RegisterNoteViewModel(OpenApiRepositoryImpl(OpenAPIDataSourceImpl(OpenApiService)))
                }
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}