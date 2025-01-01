package com.jeong.sesac.sai.viewmodel

import androidx.lifecycle.ViewModel
import com.jeong.sesac.sai.dto.ListType
import com.jeong.sesac.sai.dto.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SearchNoteViewModel() : ViewModel() {

    private var _noteList : List<Note> = emptyList()
    private var _filteredNoteList = MutableStateFlow<List<Note>>(emptyList())
    val weeklyNoteList get() = _filteredNoteList.asStateFlow()

    // repo에서 noteList를 받아옴(note_collection에 있는 쪽지 중에 userNoteInteraction에 쪽지아이디가 없는 쪽지들)

    fun getNoteList() {
        // searchNoteRepo.getSearchNoteList()
    }

    // 필터링된 리스트 프래그먼트로 보내기
    fun filteredNoteList(listType: ListType = ListType.RECENT) {
        _filteredNoteList.value = when(listType) {
            ListType.RECENT -> _noteList.sortedByDescending { it.created_at }
            ListType.LIKES_UP -> _noteList.sortedByDescending { it.likes }
            ListType.LIKES_DOWN -> _noteList.sortedBy { it.likes }
        }
    }

    fun uploadFoundNote() {
        //  searchNoteRepo.postFoundNote()
    }

    // 쪽지에 등록된 isbn과 스캔해서 나온 isbn이 맞는지 확인
    fun scanISBN() : String {
        // searchNoteRepo.checkISBN()
        return ""
    }

    fun getBookInfo(isbn : String) {
//        return BookInfo("2024021-가-32ㅇㅁㄴ", "그리스로마신화")
    }

    fun getMyFoundNote() {
        // searchNoteRepo.getMyFoundNote()
    }

   fun postReview(content: String) {
       // searchNoteRepo.postReview(content)
   }
}