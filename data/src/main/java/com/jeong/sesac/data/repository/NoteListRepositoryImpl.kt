package com.jeong.sesac.data.repository

import android.util.Log
import com.jeong.sesac.data.datasource.NoteDataSourceImpl
import com.jeong.sesac.domain.model.NoteFilterType
import com.jeong.sesac.domain.model.SortOrder
import com.jeong.sesac.domain.repository.INoteListRepository
import com.jeong.sesac.feature.model.NoteWithUser
import com.jeong.sesac.feature.model.UserInfo

class NoteListRepositoryImpl(private val noteDataSourceImpl: NoteDataSourceImpl, private val UserRepo: UserRepositoryImpl) :
    INoteListRepository {
    override suspend fun getNoteList(
        filterType: NoteFilterType,
        userId: String
    ): Result<List<NoteWithUser>> {
        return noteDataSourceImpl.getNoteList().fold(
            onSuccess = { noteList ->
                // 필터 타입에 따라 노트 리스트 필터링 및 정렬
                val filteredNoteList = when (filterType) {
                    is NoteFilterType.ThisWeek -> {
                        val weekAgo = System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000
                        val thisWeekNotes = noteList.filter {
                            it.createdAt > weekAgo
                        }

                        when (filterType.sortOrder) {
                            SortOrder.LATEST -> thisWeekNotes.sortedByDescending { it.createdAt }
                            SortOrder.LIKES_DESC -> thisWeekNotes.sortedByDescending { it.likes.size }
                            SortOrder.LIKES_ASC -> thisWeekNotes.sortedBy { it.likes.size }
                        }
                    }

                    is NoteFilterType.ByCreatedAt -> {
                        if (filterType.ascending) {
                            noteList.sortedBy { it.createdAt }
                        } else {
                            noteList.sortedByDescending { it.createdAt }
                        }
                    }

                    is NoteFilterType.ByLikes -> {
                        if (filterType.ascending) {
                            noteList.sortedBy { it.likes.size }
                        } else {
                            noteList.sortedByDescending { it.likes.size }
                        }
                    }

                    is NoteFilterType.ByLibrary -> {
                        noteList.filter { it.libraryName == filterType.libraryName }
                    }

                    is NoteFilterType.MyNotes -> {
                        noteList.filter { it.userId == userId }
                            .sortedByDescending { it.createdAt }
                    }
                    is NoteFilterType.MyLikedNotes -> {
                        noteList.filter { it.likes.contains(userId) }
                            .sortedByDescending { it.createdAt }
                    }

                }

                // 필터링된 노트에 사용자 정보 추가
                val notesWithUser = filteredNoteList.map { note ->
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
                        createdAt = note.createdAt,
                        libraryName = note.libraryName,
                        content = note.content,
                        likes = note.likes
                    )
                }
                Result.success(notesWithUser)
            },
            onFailure = { error ->
                Log.e("NoteListRepository", "error getNoteList: ${error.message}")
                Result.failure(error)
            }
        )
    }

   override suspend fun getLibraryNotes(libraryName: String): Result<List<NoteWithUser>> {
      return noteDataSourceImpl.getLibraryNotes(libraryName).map {notes ->
           notes.map { note ->
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
                   createdAt = note.createdAt,
                   libraryName = note.libraryName,
                   content = note.content,
                   likes = note.likes
               )
           }
       }.onFailure {
           throw Exception("도서관 목록을 가져오는데 실패했습니다 ${it.message}")
       }
    }


    override suspend fun toggleLike(noteId: String, userId: String): Result<Boolean> {
        return noteDataSourceImpl.toggleLike(noteId, userId)
    }
}



