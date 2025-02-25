package com.jeong.sesac.domain.repository

import com.jeong.sesac.feature.model.BookInfo

interface IBookRepository {
    suspend fun createBook(isbn: String, userId: String, library: String): Result<Unit>
    suspend fun getBookList(library: String): Result<List<BookInfo>>
    suspend fun getBook(library: String, bookId: String): Result<BookInfo>
}