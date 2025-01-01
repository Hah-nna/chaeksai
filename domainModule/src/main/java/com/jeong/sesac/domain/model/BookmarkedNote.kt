package com.jeong.sesac.domain.model

data class BookmarkedNote(
    val id : String,
    val hint_img : String,
    val hints : List<String>,
)

// info window or material popup
// rest location
// 쪽지숨기기 / 찾기 <- 프로세스 다시 생각하기