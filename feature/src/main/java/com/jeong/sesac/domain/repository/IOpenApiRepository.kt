package com.jeong.sesac.feature.repository

import com.jeong.sesac.feature.model.BookInfo

interface IOpenApiRepository {
    suspend fun getBookInfo(isbn : String): Result<String>
}