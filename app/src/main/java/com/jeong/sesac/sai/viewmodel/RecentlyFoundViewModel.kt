package com.jeong.sesac.sai.viewmodel

import androidx.lifecycle.ViewModel
import com.jeong.sesac.sai.dto.ListType
import com.jeong.sesac.sai.dto.Note
import com.jeong.sesac.sai.repository.AuthRepository
import com.jeong.sesac.sai.repository.RecentlyFoundNotesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class RecentlyFoundViewModel(private val recentNoteRepo : RecentlyFoundNotesRepository) : ViewModel() {

    // 값 변경가능한 stateFlow
    private val _noteList = MutableStateFlow<List<Note>>(emptyList())

    // Represents this mutable state flow as a read-only state flow.
    // 변경가능한 플로우를 읽기 전용 흐름으로 나타냄
    val noteList get() = _noteList.asStateFlow()

    private val _listType = MutableStateFlow<ListType>(ListType.RECENT)
    val listType get() = _listType.asStateFlow()

    // 리스트를 가져올 때
    fun loadNoteList() {
        // val notes = NoteListRepository의 getRecentNoteList()에서 받아온 값
        // 성공하면 _noteList = notes
    }

    // 필터링된 리스트 가져오기
    fun updateListType(type: ListType) {
        // ListType에 따라서 NoteRepository.getRecentNoteList()를 다르게 해줌
        //
    }

    fun getNoteDetail(id : String){
        // 아이디값 받아서 해당하는 노트의 아이디를 조회하게
        // NoteRepository에 보냄
    }

}