package com.jeong.sesac.domain.repository

import kotlinx.coroutines.flow.Flow


interface IAuthRepository {
    suspend fun getUserInfo() : Flow<String>
    suspend fun setUserInfo(nickname: String)
    suspend fun createUser(nickname: String)
}