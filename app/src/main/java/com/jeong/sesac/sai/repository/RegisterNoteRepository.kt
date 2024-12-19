package com.jeong.sesac.sai.repository

import com.jeong.sesac.sai.repository.interfaces.RegisterNoteInterface

class RegisterNoteRepository : RegisterNoteInterface {
    override fun postNote() {
        TODO("파베로 post 요청~~~")
    }

    override fun checkISBN(): Boolean {
        TODO("등록할 때 isbn과 책이름이 맞는지 확인")
    }
}