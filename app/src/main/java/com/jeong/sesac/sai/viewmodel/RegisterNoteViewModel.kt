package com.jeong.sesac.sai.viewmodel

import androidx.lifecycle.ViewModel
import com.jeong.sesac.sai.repository.RegisterNoteRepository

class RegisterNoteViewModel(private val registerNoteRepo : RegisterNoteRepository) : ViewModel() {

    // 흠 근데 여러 프래그먼트를 거치면서
    // 각각의 프래그먼트에서 정보를 받는데 그건 어떻게 함?... 어디다가 상태를 저장해야함...?
    // 띠용 그걸 모르겠음

    fun uploadNote() {
        // registerNoteRepo.postNote()로 지금 뷰모델의 상태값들을 보내야함
    }
}