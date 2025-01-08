package com.jeong.sesac.domain.repository

import com.jeong.sesac.domain.model.UserInfo
import kotlinx.coroutines.flow.Flow

interface ILoginRepository {
    suspend fun getUser(id : String)
    suspend fun setUser(nickname: String) : Boolean
    suspend fun checkDuplicateNickname(nickname : String) : Boolean
}