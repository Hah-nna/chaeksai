package com.jeong.sesac.sai.viewmodel

import androidx.lifecycle.ViewModel
import com.jeong.sesac.domain.model.NoteInteraction
import com.jeong.sesac.domain.model.TYPE
import com.jeong.sesac.sai.dto.RecordListType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class RecordViewModel() : ViewModel()  {
    private var _noteList : List<NoteInteraction> = emptyList()
    private var _filteredNoteList = MutableStateFlow<List<NoteInteraction>>(emptyList())
    val filteredNoteList get() = _filteredNoteList.asStateFlow()

    fun getNoteList(userId : String) {
        //  _noteList = recordRepo.getNoteList(id)호출
    }

    // 필터링된 리스트 프래그먼트로 보내기
    fun filteredNoteList(listType: RecordListType = RecordListType.REGISTERED) {
        _filteredNoteList.value = when(listType) {
            RecordListType.REGISTERED -> _noteList.filter { it.type == TYPE.REGISTERED }.sortedByDescending { it.created_at }
            RecordListType.FOUND -> _noteList.filter { it.type == TYPE.FOUND }.sortedByDescending { it.created_at }
            RecordListType.BOOKMARKED -> _noteList.filter { it.type == TYPE.BOOKMARKED }.sortedByDescending { it.created_at }
        }
    }

    fun getNoteDetail() {
        // recordRepo.getNoteDetail()
        // 노트정보 받아서 그 중에 보여줄 것만 보여주기
        // 왜냐면 등록한쪽지, 찾기완료한 쪽지는 똑같은데
        // 찜한 쪽지는 아직 찾기 전인 쪽지라서 보여줄게 다르기 때문
    }



}