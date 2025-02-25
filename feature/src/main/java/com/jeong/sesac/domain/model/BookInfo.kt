package com.jeong.sesac.feature.model


data class BookInfo (
    val id: String = "",
    val userId: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val library: String = "",
    val title : String = "",
    val isbn : String= "",
    val book_img : String = "",
    val author: String = "",
    val introduction: String = "",
    val publisher: String = "",
    val form: String = "",
    val publish_date: String = "",
    val score: Float = 0f,
    val page: String = ""
)


data class BookReview (
    val id: String = "",
    val userId: String = "",
    val content: String = "",
    val score: Float = 0F,
    val createdAt: Long = 0L,
    val library: String = "",
)

data class BookReviewWithUser (
    val id: String,
    val userInfo: UserInfo,
    val content: String,
    val score: Float,
    val createdAt: Long,
    val library: String,
)