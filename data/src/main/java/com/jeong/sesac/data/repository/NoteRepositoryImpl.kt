package com.jeong.sesac.data.repository

import com.jeong.sesac.data.datasource.FireBaseDataSource
import com.jeong.sesac.domain.repository.INoteRepository
import com.jeong.sesac.feature.model.Note
import com.jeong.sesac.feature.model.NoteWithUser

class NoteRepositoryImpl(private val fireBaseDataSource: FireBaseDataSource) : INoteRepository {

    override suspend fun createNote(note: Note, nickname: String): Boolean {
        val noteInfo = Note(
            id = "",
            userId = "",
            image = note.image,
            title = note.title,
            content = note.content,
            createdAt = note.createdAt,
            libraryName = note.libraryName,
            likes = 0,
            )

        return fireBaseDataSource.createNote(noteInfo, nickname)
    }

    suspend fun updateNote(noteId: String, note: Note): Result<Unit> {
        return fireBaseDataSource.updateNote(noteId, note)
    }

    suspend fun getNote(noteId: String): Result<NoteWithUser> {
        return fireBaseDataSource.getNote(noteId)
    }

    suspend fun deleteNote(noteId: String): Result<Unit> {
        return fireBaseDataSource.deleteNote(noteId)
    }
}