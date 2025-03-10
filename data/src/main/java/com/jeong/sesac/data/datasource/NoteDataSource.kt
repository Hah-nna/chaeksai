package com.jeong.sesac.data.datasource

import com.jeong.sesac.feature.model.Note

interface NoteDataSource {
    suspend fun createNote(note : Note): Result<Boolean>
    suspend fun getNote(noteId: String): Result<Note>
    suspend fun getNoteList(): Result<List<Note>>
    suspend fun getLibraryNotes(libraryName: String) : Result<List<Note>>
    suspend fun updateNote(noteId: String, note: Note): Result<Unit>
    suspend fun deleteNote(noteId: String): Result<Unit>
    suspend fun toggleLike(noteId: String, userId:String): Result<Boolean>

    // crud, toggle
}