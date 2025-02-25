package com.jeong.sesac.data.repository

import com.jeong.sesac.data.datasource.FireBaseDataSource
import com.jeong.sesac.domain.repository.INoteRepository
import com.jeong.sesac.feature.model.Note
import com.jeong.sesac.feature.model.NoteWithUser
import com.jeong.sesac.feature.model.UserInfo

class NoteRepositoryImpl(
    private val fireBaseDataSource: FireBaseDataSource,
    private val UserRepo: UserRepositoryImpl
) : INoteRepository {

    override suspend fun createNote(note: Note): Result<Boolean> {
        val noteInfo = Note(
            id = "",
            userId = note.userId,
            image = note.image,
            title = note.title,
            content = note.content,
            createdAt = note.createdAt,
            libraryName = note.libraryName,
            likes = emptyList(),
        )

        return fireBaseDataSource.createNote(noteInfo)
    }

    suspend fun updateNote(noteId: String, note: Note): Result<Unit> {
        return fireBaseDataSource.updateNote(noteId, note)
    }

    suspend fun getNote(noteId: String, userId: String): Result<NoteWithUser> {
        return fireBaseDataSource.getNote(noteId).map { note ->
            val userInfo = UserRepo.getUserInfo(userId)
            NoteWithUser(
                id = note.id,
                userInfo = UserInfo(
                    id = userInfo.id,
                    profile = userInfo.profile,
                    nickName = userInfo.nickName
                ),
                image = note.image,
                title = note.title,
                content = note.content,
                createdAt = note.createdAt,
                libraryName = note.libraryName,
                likes = note.likes
            )
        }.onFailure {
            throw Exception("${it.message}")
        }
    }

    suspend fun deleteNote(noteId: String): Result<Unit> {
        return fireBaseDataSource.deleteNote(noteId)
    }
}