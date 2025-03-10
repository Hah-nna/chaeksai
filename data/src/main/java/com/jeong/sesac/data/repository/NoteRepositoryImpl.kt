package com.jeong.sesac.data.repository

import com.jeong.sesac.data.datasource.NoteDataSource
import com.jeong.sesac.domain.repository.INoteRepository
import com.jeong.sesac.feature.model.Note
import com.jeong.sesac.feature.model.NoteWithUser
import com.jeong.sesac.feature.model.UserInfo
import com.jeong.sesac.feature.repository.IUserRepository

class NoteRepositoryImpl(
    private val noteDataSourceImpl: NoteDataSource,
    private val UserRepo: IUserRepository
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

        return noteDataSourceImpl.createNote(noteInfo)
    }

    override suspend fun updateNote(noteId: String, note: Note): Result<Unit> {
        return noteDataSourceImpl.updateNote(noteId, note)
    }

    override suspend fun getNote(noteId: String, userId: String): Result<NoteWithUser> {
        return noteDataSourceImpl.getNote(noteId).map { note ->
            val userInfo = UserRepo.getUserInfo(note.userId)
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

    override suspend fun deleteNote(noteId: String): Result<Unit> {
        return noteDataSourceImpl.deleteNote(noteId)
    }
}