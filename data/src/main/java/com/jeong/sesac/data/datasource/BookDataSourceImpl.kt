package com.jeong.sesac.data.datasource

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jeong.sesac.data.api.service.OpenAPIService
import com.jeong.sesac.feature.model.BookInfo
import kotlinx.coroutines.tasks.await
import com.google.firebase.firestore.Query
import com.jeong.sesac.feature.model.BookReview

class BookDataSourceImpl(private val openApiService: OpenAPIService) : BookDataSource {
    private val bookRef = Firebase.firestore.collection("books")

    override suspend fun createBook(isbn: String, userId: String, library: String): Result<Unit> {
        return runCatching {
            val response = openApiService.getBookInfo(isbn = isbn)

            if (!response.isSuccessful) {
                throw Exception("책 정보를 받아오는데 실패했습니다, ${response.message()}")
            }

            val bookInfoResponse = response.body()?.docs?.firstOrNull()
                ?: throw Exception("isbn값으로 책을 찾지 못했습니다\n다시 시도해주세요 ${isbn}")

            val bookInfo = BookInfo(
                id = bookRef.document().id,
                userId = userId,
                createdAt = System.currentTimeMillis(),
                library = library,
                title = bookInfoResponse.title,
                isbn = isbn,
                book_img = bookInfoResponse.book_img,
                author = bookInfoResponse.author,
                introduction = bookInfoResponse.introduction,
                publisher = bookInfoResponse.publisher,
                publish_date = bookInfoResponse.publish_date,
                form = bookInfoResponse.form,
                page = bookInfoResponse.page
            )
            bookRef.document(bookInfo.id).set(bookInfo).await()
        }
    }

    override suspend fun getBookList(library: String): Result<List<BookInfo>> {
        return runCatching {
            val result = bookRef.whereEqualTo("library", library)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get().await()

            result.documents.mapNotNull { it.toObject(BookInfo::class.java) }
        }.onFailure {
            Log.e("error", "${it.message}, ${it.cause}")
        }
    }

    override suspend fun getBook(library: String, bookId: String): Result<BookInfo> {
        return runCatching {
            val result = bookRef.document(bookId).get().await()

            result.toObject(BookInfo::class.java)
                ?: throw Exception("해당 책을 찾을 수 없습니다")
        }.onFailure {
            throw Exception("책 가져오기 실패~~")
        }
    }

    override suspend fun createBookReview(bookReview: BookReview, bookId: String): Result<Boolean> {
        return runCatching {
            val bookReviewDocRef = bookRef
                .document(bookId)
                .collection("book_reviews")
                .add(bookReview)
                .await()

            bookRef.document(bookId)
                .collection("book_reviews")
                .document(bookReviewDocRef.id)
                .update("id", bookReviewDocRef.id)
                .await()
            true
        }.onFailure {e ->
            throw Exception("북리뷰 생성 에러 ${e.message}, ${e.cause}")
        }
    }

    override suspend fun getBookReviews(bookId: String): Result<MutableList<BookReview>> {
       return runCatching {
        val reviews = bookRef.document(bookId)
            .collection("book_reviews")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .get()
            .await()


           reviews.toObjects(BookReview::class.java) as MutableList<BookReview>

       }.onFailure {e ->
           Log.e("리뷰가져오기에러", "${e.message}, ${e.cause}")
           throw Exception(e.message)
       }
    }

    override suspend fun updateBookReview(bookId: String, reviewId: String, content: String, score: Float): Result<Boolean> {
        return runCatching {
        bookRef.document(bookId)
            .collection("book_reviews")
            .document(reviewId)
            .update("content", content, "score", score)
            .await()
            true
        }.onFailure {
            Log.e("리뷰업데이트에러", "${it.cause}, ${it.message}")
            throw Exception("리뷰 수정에 실패하였습니다")
        }

    }

   override suspend fun deleteBookReview(bookId: String, reviewId: String): Result<Boolean> {
        return runCatching {
            bookRef.document(bookId)
                .collection("book_reviews")
                .document(reviewId)
                .delete()
                .await()
            true
        }.onFailure {
            Log.e("리뷰삭제에러", "${it.cause}, ${it.message}")
            throw Exception("리뷰 삭제에 실패하였습니다")
        }
    }
}