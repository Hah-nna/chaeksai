package com.jeong.sesac.data.repository

import com.jeong.sesac.data.datasource.FireBaseDataSourceImpl
import com.jeong.sesac.feature.model.ProviderType
import com.jeong.sesac.feature.model.User
import com.jeong.sesac.feature.repository.ILoginRepository

class LoginRepositoryImpl(private val fireStoreImpl: FireBaseDataSourceImpl) : ILoginRepository {

    override suspend fun setUser(nickname: String): Result<String> {

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


    override suspend fun checkDuplicateNickname(nickname: String): Result<Boolean> {
        return fireStoreImpl.getDuplicateNickname(nickname)
    }
}