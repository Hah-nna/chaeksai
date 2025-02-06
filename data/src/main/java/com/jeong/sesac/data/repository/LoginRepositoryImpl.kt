package com.jeong.sesac.data.repository

import com.jeong.sesac.data.datasource.FireBaseDataSourceImpl
import com.jeong.sesac.feature.model.ProviderType
import com.jeong.sesac.feature.model.User
import com.jeong.sesac.feature.repository.ILoginRepository

class LoginRepositoryImpl(private val fireStoreImpl: FireBaseDataSourceImpl) : ILoginRepository {

    override suspend fun getUser(id: String) {
        TODO("파베에서 id받아서 가져오기")
    }

    override suspend fun setUser(nickname: String) : Boolean {

        val userInfo = User(
            id = "",
            nickname = nickname,
            profile = "",
            // 이건 유저 등록할 떄 업데이트
            created_at = System.currentTimeMillis(),
            provider_info = ProviderType.KAKAO
        )
        return fireStoreImpl.createUser(userInfo)
    }


    override suspend fun checkDuplicateNickname(nickname: String): Boolean {
        return fireStoreImpl.getDuplicateNickname(nickname)
    }
}