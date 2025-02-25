package com.jeong.sesac.data.repository

import com.jeong.sesac.data.datasource.FireBaseDataSourceImpl
import com.jeong.sesac.feature.model.UserInfo

class UserRepositoryImpl(private val firebaseDataSource: FireBaseDataSourceImpl) {

    suspend fun getUserInfo(userId: String): UserInfo {
       return firebaseDataSource.getUserInfo(userId).let {
           it.copy(id = userId, profile = it.profile, nickName = it.nickName)
       }
    }
}