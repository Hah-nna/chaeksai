package com.jeong.sesac.sai.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class RegisterNoteViewModel() : ViewModel() {
    private var _noteId =  MutableStateFlow<String?>(null)
    val noteId = _noteId.asStateFlow()

    private var _libraryName = MutableStateFlow<String?>(null)
    val libraryName = _libraryName.asStateFlow()

    private var _isbn = MutableStateFlow<String?>(null)
    val isbn = _isbn.asStateFlow()

    private var _bookName = MutableStateFlow<String?>(null)
    val bookName = _bookName.asStateFlow()

    private var _noteContent = MutableStateFlow<String?>(null)
    val noteContent = _noteContent.asStateFlow()

    // 이미지는 디렉토리에 저장하기 -> 여기에는 디렉토리의 주소값만
    private var _hint_img = MutableStateFlow<String?>(null)
    val hint_img = _hint_img.asStateFlow()

    private var _hints = MutableStateFlow<List<String>>(emptyList())
    val hints = _hints.asStateFlow()


    fun updateLibrary(libraryName: String) {
        _libraryName.value = libraryName
    }

    fun updateNoteContent(content: String) {
        _noteContent.value = content
    }

    // 이미지랑 힌트랑 같이 업뎃
    fun updateHint(imgUrl: String, hint1: String, hint2: String, hint3: String) {
        _hint_img.value = imgUrl
        _hints.value = listOf(hint1,hint2,hint3).filter { it.isNotBlank() }
    }

    fun updateISBN() : String {
        // registerNoteRepo.scanIsbn()
        // _isbn.value = 뭔가받아온값이겠지
        return ""
    }

    fun checkBookInfo(isbn: String)  {
        // registerNoteRepo.postNote(isbn)
        // 책이름 받아옴
    }

    fun postNote() {
        // RegisterNote(libraryName, isbn, bookName, noteContent, hint_img, hints)
        // registerNoteRepo.postNote(RegisterNote)
        // 등록되고나서 아이디값 저장하기
    }

    fun getPostedNote(id : String) {
        // registerNoteRepo.getPostedNote(id)
    }



}