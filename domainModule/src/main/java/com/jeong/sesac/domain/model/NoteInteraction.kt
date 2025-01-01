package com.jeong.sesac.domain.model

import java.time.LocalDateTime

data class NoteInteraction (
    val userId : String,
    val noteId : String,
    val type : TYPE,
    val created_at : LocalDateTime
)

enum class TYPE {
    REGISTERED,
    FOUND,
    BOOKMARKED
}