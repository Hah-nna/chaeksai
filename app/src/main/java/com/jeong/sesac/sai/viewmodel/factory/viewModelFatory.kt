package com.jeong.sesac.sai.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jeong.sesac.data.api.manager.KakaoMapManager
import com.jeong.sesac.data.api.manager.OpenAPIManager
import com.jeong.sesac.data.datasource.BookDataSourceImpl
import com.jeong.sesac.data.datasource.CommentFirebaseDataSourceImpl
import com.jeong.sesac.data.datasource.FireBaseDataSourceImpl
import com.jeong.sesac.data.datasource.FireBaseStorageDataSourceImpl
import com.jeong.sesac.data.datasource.KakaoMapDataSourceImpl
import com.jeong.sesac.data.datasource.OpenAPIDataSourceImpl
import com.jeong.sesac.data.repository.BookRepositoryImpl
import com.jeong.sesac.data.repository.CommentRepositoryImpl
import com.jeong.sesac.data.repository.KakaoMapRepositoryImpl
import com.jeong.sesac.data.repository.LBSRepositoryImpl
import com.jeong.sesac.data.repository.LoginRepositoryImpl
import com.jeong.sesac.data.repository.NoteListRepositoryImpl
import com.jeong.sesac.data.repository.NoteRepositoryImpl
import com.jeong.sesac.data.repository.OpenApiRepositoryImpl
import com.jeong.sesac.data.repository.UserRepositoryImpl
import com.jeong.sesac.sai.util.ChakSaiClass
import com.jeong.sesac.sai.viewmodel.BookViewModel
import com.jeong.sesac.sai.viewmodel.CommentViewModel
import com.jeong.sesac.sai.viewmodel.KakaoMapViewModel
import com.jeong.sesac.sai.viewmodel.LoginViewModel
import com.jeong.sesac.sai.viewmodel.NoteListViewModel
import com.jeong.sesac.sai.viewmodel.NoteViewModel

@Suppress("UNCHECKED_CAST")
val appViewModelFactory = object : ViewModelProvider.Factory {
    val context = ChakSaiClass.getContext()
    override fun < T : ViewModel>  create(modelClass: Class<T>): T =
        with(modelClass) {
            when {
                isAssignableFrom(LoginViewModel::class.java) ->
                    LoginViewModel(LoginRepositoryImpl(FireBaseDataSourceImpl(FireBaseStorageDataSourceImpl(context))))
                isAssignableFrom(KakaoMapViewModel::class.java) -> {
                    val kakaoService = KakaoMapManager.getInstance()
                    KakaoMapViewModel(KakaoMapRepositoryImpl(KakaoMapDataSourceImpl(kakaoService)), LBSRepositoryImpl(context))
                }
                isAssignableFrom(NoteViewModel::class.java) -> {
                    NoteViewModel(NoteRepositoryImpl(FireBaseDataSourceImpl(FireBaseStorageDataSourceImpl(context)),  UserRepositoryImpl(FireBaseDataSourceImpl(FireBaseStorageDataSourceImpl(context)))))
                }

                isAssignableFrom(NoteListViewModel::class.java) -> {
                    NoteListViewModel(NoteListRepositoryImpl(FireBaseDataSourceImpl(FireBaseStorageDataSourceImpl(context)),  UserRepositoryImpl(FireBaseDataSourceImpl(FireBaseStorageDataSourceImpl(context)))))
                }

                isAssignableFrom(CommentViewModel::class.java) -> {
                    CommentViewModel(CommentRepositoryImpl(CommentFirebaseDataSourceImpl(FireBaseDataSourceImpl(FireBaseStorageDataSourceImpl(context))), UserRepositoryImpl(FireBaseDataSourceImpl(FireBaseStorageDataSourceImpl(context)))))
                }
                isAssignableFrom(BookViewModel::class.java) -> {
                    val openApiService = OpenAPIManager.getInstance()
                    BookViewModel((BookRepositoryImpl(BookDataSourceImpl(openApiService), UserRepositoryImpl(
                        FireBaseDataSourceImpl(FireBaseStorageDataSourceImpl(context))
                    ))), OpenApiRepositoryImpl(
                        OpenAPIDataSourceImpl(openApiService)
                    ))
                }
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}