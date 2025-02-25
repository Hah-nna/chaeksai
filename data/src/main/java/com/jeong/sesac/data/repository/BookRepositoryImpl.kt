package com.jeong.sesac.data.repository

import com.jeong.sesac.data.datasource.BookDataSourceImpl
import com.jeong.sesac.domain.repository.IBookRepository
import com.jeong.sesac.feature.model.BookInfo
import com.jeong.sesac.feature.model.BookReview
import com.jeong.sesac.feature.model.BookReviewWithUser
import com.jeong.sesac.feature.model.UserInfo

class BookRepositoryImpl(private val bookDataSource: BookDataSourceImpl, private val userRepo: UserRepositoryImpl ) : IBookRepository {
    override suspend fun createBook(isbn: String, userId: String, library: String): Result<Unit> {
        return bookDataSource.createBook(isbn, userId, library)
    }

  override suspend fun getBookList(library: String): Result<List<BookInfo>> {
        return bookDataSource.getBookList(library)
    }

  override suspend fun getBook(library: String, bookId: String): Result<BookInfo> {
        return bookDataSource.getBook(library, bookId)
    }

    suspend fun createBookReview(userId: String, content: String, bookId: String, score: Float, library: String): Result<Boolean> {
        val bookReview = BookReview(
            id = "",
            userId = userId,
            content = content,
            score = score,
            createdAt = System.currentTimeMillis(),
            library = library,
        )
        return bookDataSource.createBookReview(bookReview, bookId).onSuccess {
            Result.success(true)
        }.onFailure {e ->
            throw Exception(e.message)
        }
    }

    suspend fun getBookReviews(bookId: String): Result<List<BookReviewWithUser>> {
        return runCatching {
            val result = bookDataSource.getBookReviews(bookId).getOrThrow()
            result.map { review ->
                val userInfo = userRepo.getUserInfo(review.userId)
                BookReviewWithUser(
                    id = review.id,
                    userInfo = UserInfo(
                        id = review.userId,
                        profile = userInfo.profile,
                        nickName = userInfo.nickName
                    ),
                    content = review.content,
                    score = review.score,
                    createdAt = review.createdAt,
                    library = review.library
                )
            }
        }
    }

    suspend fun updateBookReview(bookId: String, reviewId: String, content: String, score: Float): Result<Boolean> {
       return bookDataSource.updateBookReview(bookId, reviewId, content, score)
    }

    suspend fun deleteBookReview(bookId: String, reviewId: String): Result<Boolean> {
        return bookDataSource.deleteBookReview(bookId, reviewId)

    }
}