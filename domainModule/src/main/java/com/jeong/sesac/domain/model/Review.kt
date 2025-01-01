package com.jeong.sesac.domain.model

import java.time.LocalDateTime

data class Review(
    val id: String,
    val content: String,
    val created_at: LocalDateTime,
    val target_id: String,
    val writer_id: String // 이건 지금 사용자를 말함
)