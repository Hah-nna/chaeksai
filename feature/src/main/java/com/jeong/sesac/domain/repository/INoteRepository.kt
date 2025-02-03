package com.jeong.sesac.domain.repository

import com.jeong.sesac.feature.model.Note

interface INoteRepository{
    suspend fun createNote(note: Note, nickname: String): Boolean
}