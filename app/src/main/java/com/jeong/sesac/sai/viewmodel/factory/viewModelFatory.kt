package com.jeong.sesac.sai.viewmodel.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jeong.sesac.data.api.manager.KakaoMapManager
import com.jeong.sesac.data.api.manager.OpenAPIManager
import com.jeong.sesac.data.datasource.FireBaseDataSourceImpl
import com.jeong.sesac.data.datasource.FireBaseStorageDataSourceImpl
import com.jeong.sesac.data.datasource.KakaoMapDataSourceImpl
import com.jeong.sesac.data.datasource.OpenAPIDataSourceImpl
import com.jeong.sesac.data.repository.KakaoMapRepositoryImpl
import com.jeong.sesac.data.repository.LoginRepositoryImpl
import com.jeong.sesac.data.repository.NoteListRepositoryImpl
import com.jeong.sesac.data.repository.OpenApiRepositoryImpl
import com.jeong.sesac.data.repository.WriteNoteRepositoryImpl
import com.jeong.sesac.sai.util.AppPreferenceManager
import com.jeong.sesac.sai.util.ChakSaiClass
import com.jeong.sesac.sai.viewmodel.KakaoMapViewModel
import com.jeong.sesac.sai.viewmodel.LoginViewModel
import com.jeong.sesac.sai.viewmodel.NoteListViewModel
import com.jeong.sesac.sai.viewmodel.WriteNoteViewModel

@Suppress("UNCHECKED_CAST")
val appViewModelFactory = object : ViewModelProvider.Factory {
    override fun < T : ViewModel>  create(modelClass: Class<T>): T =
        with(modelClass) {
            when {
                isAssignableFrom(LoginViewModel::class.java) ->
                    LoginViewModel(LoginRepositoryImpl(FireBaseDataSourceImpl(FireBaseStorageDataSourceImpl(ChakSaiClass.getContext()))))
                isAssignableFrom(KakaoMapViewModel::class.java) -> {
                    val kakaoService = KakaoMapManager.getInstance()
                    KakaoMapViewModel(KakaoMapRepositoryImpl(KakaoMapDataSourceImpl(kakaoService)))
                }
                isAssignableFrom(WriteNoteViewModel::class.java) -> {
                    WriteNoteViewModel(WriteNoteRepositoryImpl(FireBaseDataSourceImpl(FireBaseStorageDataSourceImpl(ChakSaiClass.getContext()))))
                }

                isAssignableFrom(NoteListViewModel::class.java) -> {
                    NoteListViewModel(NoteListRepositoryImpl(FireBaseDataSourceImpl(FireBaseStorageDataSourceImpl(ChakSaiClass.getContext()))))
                }
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}