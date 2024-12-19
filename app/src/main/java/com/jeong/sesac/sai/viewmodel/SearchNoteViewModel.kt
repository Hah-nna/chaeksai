package com.jeong.sesac.sai.viewmodel

import androidx.lifecycle.ViewModel
import com.jeong.sesac.sai.repository.SearchNoteRepository

class SearchNoteViewModel(private val searchNoteRepo : SearchNoteRepository) : ViewModel() {

    fun uploadFoundNote() {
       /**
        * searchNoteRepo.postFoundNote()로 찾은 상태 보내기
        * 근데 내가등록, 내가찾기완료, 찜한쪽지 <- 이 상태를 어디다가 저장?
        * 그리고 이 상태들이 쪽지에 속하는건데 data class Note의 프로퍼티로 넣어야하는건지...?
        * 헷갈려어ㅓㅓ
        * */
    }
}