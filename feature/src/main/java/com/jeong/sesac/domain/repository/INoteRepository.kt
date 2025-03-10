package com.jeong.sesac.domain.repository

import com.jeong.sesac.feature.model.Note
import com.jeong.sesac.feature.model.NoteWithUser

interface INoteRepository{
    suspend fun createNote(note: Note): Result<Boolean>
    suspend fun updateNote(noteId: String, note: Note): Result<Unit>
    suspend fun getNote(noteId: String, userId: String): Result<NoteWithUser>
    suspend fun deleteNote(noteId: String): Result<Unit>
}