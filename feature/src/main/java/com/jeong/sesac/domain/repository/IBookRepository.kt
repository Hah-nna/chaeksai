package com.jeong.sesac.domain.repository

import com.jeong.sesac.feature.model.BookInfo
import com.jeong.sesac.feature.model.BookReviewWithUser

interface IBookRepository {
    suspend fun createBook(isbn: String, userId: String, library: String): Result<Unit>
    suspend fun getBookList(library: String): Result<List<BookInfo>>
    suspend fun getBook(library: String, bookId: String): Result<BookInfo>
    suspend fun createBookReview(userId: String, content: String, bookId: String, score: Float, library: String): Result<Boolean>
    suspend fun getBookReviews(bookId: String): Result<List<BookReviewWithUser>>
    suspend fun updateBookReview(bookId: String, reviewId: String, content: String, score: Float): Result<Boolean>
    suspend fun deleteBookReview(bookId: String, reviewId: String): Result<Boolean>
}