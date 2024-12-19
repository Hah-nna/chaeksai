package com.jeong.sesac.sai.repository.interfaces

interface RegisterNoteInterface {
    fun postNote()
    fun checkISBN(): Boolean
}