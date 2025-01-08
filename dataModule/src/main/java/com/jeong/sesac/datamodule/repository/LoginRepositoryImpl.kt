package com.jeong.sesac.datamodule.repository

import android.annotation.SuppressLint
import com.jeong.sesac.datamodule.datasource.FireBaseDataSourceImpl
import com.jeong.sesac.domain.model.ProviderType
import com.jeong.sesac.domain.model.UserInfo
import com.jeong.sesac.domain.repository.ILoginRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

class LoginRepositoryImpl(private val fireStoreImpl: FireBaseDataSourceImpl) : ILoginRepository {

    override suspend fun getUser(id: String) {
        TODO("파베에서 id받아서 가져오기")
    }

    @SuppressLint("NewApi")
    override suspend fun setUser(nickname: String) : Boolean {
        val userInfo = UserInfo(
            userId = "",
            nickName = nickname,
            profile_img = "",
            created_at = LocalDateTime.now().toString(),
            hint_ticket = 0,
            hint_exchange_ticket = 0,
            provider_info = ProviderType.KAKAO
        )
        return fireStoreImpl.createUser(userInfo)
    }


    override suspend fun checkDuplicateNickname(nickname: String): Boolean {
        return fireStoreImpl.getDuplicateNickname(nickname)
    }
}