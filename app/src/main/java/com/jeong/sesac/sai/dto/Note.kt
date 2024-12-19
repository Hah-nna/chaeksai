package com.jeong.sesac.sai.dto

import java.util.Locale

data class Note(
    val id: String,
    val book_name: String,
    val content: String,
    val created_at: Locale,
    val finder_id: String,
    val isbn: String,
    val library_name: String,
    val likes: Int,
    val writer_id: String,
    val hint_img: String,
    val hints: List<String>
)


enum class ListType {
    RECENT,
    LIKES_UP,
    LIKES_DOWN
}