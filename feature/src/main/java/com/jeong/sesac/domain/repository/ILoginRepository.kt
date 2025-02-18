package com.jeong.sesac.feature.repository


interface ILoginRepository {
    suspend fun setUser(nickname: String): Result<String>
    suspend fun checkDuplicateNickname(nickname : String): Result<Boolean>
}