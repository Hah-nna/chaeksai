package com.jeong.sesac.sai.viewmodel

import androidx.lifecycle.ViewModel
import com.jeong.sesac.sai.dto.ListType
import com.jeong.sesac.sai.dto.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class WeeklyNotesViewModel() : ViewModel() {
    private var _noteList : List<Note> = emptyList()
    private var _filteredNoteList = MutableStateFlow<List<Note>>(emptyList())
    val weeklyNoteList get() = _filteredNoteList.asStateFlow()

    fun getWeeklyNoteList() {
        // val weeklyNotess = weeklyNoteRepo.getWeeklyNotes()호출
    }

    // 필터링된 리스트 프래그먼트로 보내기
    fun filteredNoteList(listType: ListType = ListType.RECENT) {
        _filteredNoteList.value = when(listType) {
            ListType.RECENT -> _noteList.sortedByDescending { it.created_at }
            ListType.LIKES_UP -> _noteList.sortedByDescending { it.likes }
            ListType.LIKES_DOWN -> _noteList.sortedBy { it.likes }
        }
    }

    fun getNoteDetail(id : String){
        // 아이디값 받아서 해당하는 노트의 아이디를 조회하게
        // WeeklyNotesRepository에 보냄
    }
}