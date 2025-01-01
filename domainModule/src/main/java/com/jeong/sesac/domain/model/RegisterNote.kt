package com.jeong.sesac.domain.model

data class RegisterNote(
    val id : String,
    val libraryName: String,
    val noteContent : String,
    val hint_img : String,
    val hints : List<String>,
    val isbn : String,
    val bookName : String
)