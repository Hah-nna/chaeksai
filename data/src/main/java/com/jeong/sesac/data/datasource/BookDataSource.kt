package com.jeong.sesac.data.datasource

import com.jeong.sesac.feature.model.BookInfo
import com.jeong.sesac.feature.model.BookReview

interface BookDataSource {
    suspend fun createBook(isbn: String, userId: String, library: String): Result<Unit>
    suspend fun getBookList(library: String): Result<List<BookInfo>>
    suspend fun getBook(library: String, bookId: String): Result<BookInfo>
    suspend fun createBookReview(bookReview: BookReview, bookId: String): Result<Boolean>
    suspend fun getBookReviews(bookId: String): Result<MutableList<BookReview>>
    suspend fun updateBookReview(bookId: String, reviewId: String, content: String, score: Float): Result<Boolean>
    suspend fun deleteBookReview(bookId: String, reviewId: String): Result<Boolean>
}