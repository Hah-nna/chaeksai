package com.jeong.sesac.domain.repository

import com.jeong.sesac.domain.model.BookInfo

interface IOpenApiRepository {
    suspend fun getBookInfo(isbn : String ) : List<BookInfo>
}