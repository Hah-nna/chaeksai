package com.jeong.sesac.datamodule.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.jeong.sesac.datamodule.datasource.DataStoreDataSource
import com.jeong.sesac.datamodule.datasource.FireBaseDataSourceImpl
import com.jeong.sesac.datamodule.datasource.PreferenceDataStoreConstants
import com.jeong.sesac.domain.model.ProviderType
import com.jeong.sesac.domain.model.UserInfo
import com.jeong.sesac.domain.repository.IAuthRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime
import java.util.Date

class AuthRepositoryImpl(
    private val dataStoreDataManager: DataStoreDataSource,
    private val fireStoreImpl : FireBaseDataSourceImpl
) : IAuthRepository {
    override suspend fun getUserInfo(): Flow<String> =
        dataStoreDataManager.getUserName(PreferenceDataStoreConstants.USER_NICKNAME, "")

    override suspend fun setUserInfo(nickname: String) {
        dataStoreDataManager.setUserName(PreferenceDataStoreConstants.USER_NICKNAME, nickname)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun createUser(nickname: String) {
        val userInfo = UserInfo(
            userId = "",
            nickName = nickname,
            profile_img = "",
            created_at = LocalDateTime.now().toString(),
            hint_ticket = 0,
            hint_exchange_ticket = 0,
            provider_info = ProviderType.KAKAO
        )
        fireStoreImpl.createUser(userInfo)
    }
}