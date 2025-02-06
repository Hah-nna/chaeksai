package com.jeong.sesac.data.repository

import com.jeong.sesac.data.datasource.FireBaseDataSourceImpl
import com.jeong.sesac.domain.model.NoteFilterType
import com.jeong.sesac.domain.model.SortOrder
import com.jeong.sesac.domain.repository.INoteListRepository
import com.jeong.sesac.feature.model.NoteWithUser

class NoteListRepositoryImpl(private val fireBaseDataSource: FireBaseDataSourceImpl) :
    INoteListRepository {
    override suspend fun getNoteList(
        filterType: NoteFilterType,
        nickname: String?
    ): Result<List<NoteWithUser>> {
        return try {
            // 전체 노트 리스트 가져오기
            val noteList = fireBaseDataSource.getNoteList()

            val userId = when (filterType) {
                is NoteFilterType.MyNotes, is NoteFilterType.MyLikedNotes -> {
                    nickname?.let { fireBaseDataSource.getIdByNickname(it) }
                }
                else -> null
            }

            // 타입에 따라서 쪽지리스트 필터링
            val filteredNoteList = when (filterType) {
                is NoteFilterType.ThisWeek -> {
                    val weekAgo = System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000
                    val thisWeekNotes = noteList.filter {
                        it.createdAt > weekAgo
                    }

                    when (filterType.sortOrder) {
                        SortOrder.LATEST -> thisWeekNotes.sortedByDescending { it.createdAt }
                        SortOrder.LIKES_DESC -> thisWeekNotes.sortedByDescending { it.likes }
                        SortOrder.LIKES_ASC -> thisWeekNotes.sortedBy { it.likes }
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
                        noteList.sortedBy { it.likes }
                    } else {
                        noteList.sortedByDescending { it.likes }
                    }
                }

                is NoteFilterType.ByLibrary -> {
                    noteList.filter { it.libraryName == filterType.libraryName }
                }

                is NoteFilterType.AllLibrary -> {
                    noteList.sortedByDescending { it.createdAt }
                }

                is NoteFilterType.MyNotes -> {
                    noteList.filter { it.userId == userId }.sortedByDescending { it.createdAt }
                }

                is NoteFilterType.MyLikedNotes -> {
                    val likedNoteIdList = fireBaseDataSource.getMyLikedNotes(userId!!)
                    noteList.filter { it.id in likedNoteIdList }.sortedByDescending { it.createdAt }

                }
            }

            val notesWithUser = filteredNoteList.map { note ->
                val userInfo = fireBaseDataSource.getUserInfo(note.userId)
                NoteWithUser(
                    id = note.id,
                    userInfo = userInfo,
                    image = note.image,
                    title = note.title,
                    createdAt = note.createdAt,
                    libraryName = note.libraryName,
                    content = note.content,
                    likes = note.likes
                )
            }

            Result.success(notesWithUser)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}



