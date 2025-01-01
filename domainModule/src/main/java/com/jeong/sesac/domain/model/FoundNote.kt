package com.jeong.sesac.domain.model

import java.time.LocalDateTime

data class FoundNote(
    // 내가 찾은 쪽지 - 힌트이미지, 찾은 날짜, 도서관명, 쪽지내용
    val hint_img : String,
    val date : LocalDateTime,
    val libraryName : String,
    val noteContent: String
)