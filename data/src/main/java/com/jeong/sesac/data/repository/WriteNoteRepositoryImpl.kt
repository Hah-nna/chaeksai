package com.jeong.sesac.data.repository

import com.jeong.sesac.data.datasource.FireBaseDataSource
import com.jeong.sesac.domain.repository.INoteRepository
import com.jeong.sesac.feature.model.Note

class WriteNoteRepositoryImpl(private val fireBaseDataSource: FireBaseDataSource) : INoteRepository {

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
}