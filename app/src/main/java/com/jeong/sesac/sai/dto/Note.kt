package com.jeong.sesac.sai.dto

import java.util.Locale

data class Note(
    val id: String,
    val created_at: Locale,
    val content: String,
    val library_name: String,
    val book_name: String,
    val isbn: String,
    val finder_id: String,
    val writer_id: String,
    val likes: Int,
)


enum class ListType {
    RECENT,
    LIKES_UP,
    LIKES_DOWN
}