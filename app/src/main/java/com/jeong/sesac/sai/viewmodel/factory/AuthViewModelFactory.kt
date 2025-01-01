package com.jeong.sesac.sai.viewmodel.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jeong.sesac.datamodule.datasource.DataStoreDataSourceImpl
import com.jeong.sesac.datamodule.datasource.FireBaseDataSourceImpl
import com.jeong.sesac.datamodule.repository.AuthRepositoryImpl
import com.jeong.sesac.sai.viewmodel.AuthViewModel

/**
 * ViewModelProvider.Factory를 구현하는 익명객체
 * 인터페이스 Factory의 create를 오버라이드함
 *
 * */
class AuthViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(
                    AuthRepositoryImpl(
                        DataStoreDataSourceImpl(context),
                        FireBaseDataSourceImpl()
                    )
                ) as T
            }

            else -> throw IllegalArgumentException("알려지지 않은 뷰모델")
        }
    }
}