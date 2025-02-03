package com.jeong.sesac.domain.repository

import com.jeong.sesac.domain.model.NoteFilterType
import com.jeong.sesac.feature.model.NoteWithUser

interface INoteListRepository {
    suspend fun getNoteList(filterType: NoteFilterType, nickname: String? = null): Result<List<NoteWithUser>>
}