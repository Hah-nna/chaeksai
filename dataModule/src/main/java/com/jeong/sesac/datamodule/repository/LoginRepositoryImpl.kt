package com.jeong.sesac.datamodule.repository

import com.jeong.sesac.datamodule.datasource.FireBaseDataSourceImpl
import com.jeong.sesac.domain.model.UserInfo
import com.jeong.sesac.domain.repository.ILoginRepository

class LoginRepositoryImpl(private val fireStoreImpl : FireBaseDataSourceImpl) : ILoginRepository {

    override suspend fun getUser(id: String) {
        TODO("파베에서 id받아서 가져오기")
    }

    override suspend fun setUser(userInfo: UserInfo) : Boolean {
       return fireStoreImpl.createUser(userInfo)
    }

    override suspend fun checkDuplicateNickname(nickname : String) : Boolean {
        return fireStoreImpl.getDuplicateNickname(nickname)
    }
}