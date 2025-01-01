package com.jeong.sesac.domain.model

data class BookInfo (
    val page_no : Int = 1,
    val title : String,
    val isbn : String,
    val book_img : String
) {
    fun toMap() : Map<String, Any> {
        return mapOf(
            "page_no" to page_no,
            "title" to title,
            "isbn" to isbn,
            "book_img" to book_img
        )
    }
}