package com.jeong.sesac.feature.repository


interface ILoginRepository {
    suspend fun getUser(id : String)
    suspend fun setUser(nickname: String) : Boolean
    suspend fun checkDuplicateNickname(nickname : String) : Boolean
}