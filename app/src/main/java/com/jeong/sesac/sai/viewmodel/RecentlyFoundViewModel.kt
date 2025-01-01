package com.jeong.sesac.sai.viewmodel

import androidx.lifecycle.ViewModel
import com.jeong.sesac.sai.dto.ListType
import com.jeong.sesac.sai.dto.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class RecentlyFoundViewModel() : ViewModel() {

    private var _noteList : List<Note> = emptyList()

    private var _filteredNoteList = MutableStateFlow<List<Note>>(emptyList())

    // 공식 설명 : Represents this mutable state flow as a read-only state flow.
    // asStateFlow() : 변경가능한 플로우를 읽기 전용 흐름으로 나타냄
    val noteList = _filteredNoteList.asStateFlow()

    // repo에서 찾기완료한 리스트를 가져올 때
    fun getNoteList() {
        // val notes = NoteListRepository의 getRecentNoteList()에서 받아온 값
    }

    // 필터링된 리스트 프래그먼트로 보내기
    fun filteredNoteList(listType: ListType = ListType.RECENT) {
        _filteredNoteList.value = when(listType) {
            ListType.RECENT -> _noteList.sortedByDescending { it.created_at }
            ListType.LIKES_UP -> _noteList.sortedByDescending { it.likes }
            ListType.LIKES_DOWN -> _noteList.sortedBy { it.likes }
        }
    }

    // repo에서 sort해서 던져주기(3개의 상태로)

    fun getNoteDetail(id : String){
        // 아이디값 받아서 해당하는 노트의 아이디를 조회하게
        // NoteRepository에 보냄
    }

}