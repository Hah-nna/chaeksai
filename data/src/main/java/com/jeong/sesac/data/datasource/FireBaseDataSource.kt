package com.jeong.sesac.data.datasource

import com.jeong.sesac.feature.model.Note
import com.jeong.sesac.feature.model.NoteWithUser
import com.jeong.sesac.feature.model.User
import com.jeong.sesac.feature.model.UserInfo


interface FireBaseDataSource {
      suspend fun createUser(userInfo: User) : Result<String>
      suspend fun getDuplicateNickname(nickname : String): Result<Boolean>
      suspend fun createNote(note : Note): Result<Boolean>
      suspend fun getNoteList(): Result<List<Note>>
      suspend fun getUserInfo(userId: String): UserInfo?
      suspend fun getIdByNickname(nickname: String): String?
//      suspend fun getMyLikedNotes(userId: String): Result<List<NoteWithUser>>
      suspend fun getLibraryNotes(libraryName: String) : Result<List<Note>>
      suspend fun getNote(noteId: String): Result<Note>
      suspend fun updateNote(noteId: String, note: Note): Result<Unit>
      suspend fun deleteNote(noteId: String): Result<Unit>
      suspend fun toggleLike(noteId: String, userId:String): Result<Boolean>
}