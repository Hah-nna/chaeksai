package com.jeong.sesac.sai.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeong.sesac.datamodule.repository.OpenApiRepositoryImpl
import com.jeong.sesac.domain.model.BookInfo
import com.jeong.sesac.sai.viewmodel.entity.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterNoteViewModel(private val openApiRepo : OpenApiRepositoryImpl) : ViewModel() {

    private var _bookTitleISBNState = MutableStateFlow<UiState<List<BookInfo>>>(UiState.Loading)
    val bookTitleISBNState = _bookTitleISBNState.asStateFlow()

    private var _postNote = MutableStateFlow<UiState<Boolean>>(UiState.Loading)
    val postNote = _postNote.asStateFlow()

     fun getBookInfo(isbn : String) = viewModelScope.launch {

         runCatching {
         _bookTitleISBNState.value = UiState.Loading
             openApiRepo.getBookInfo(isbn)
         }.onSuccess {
             _bookTitleISBNState.value = UiState.Success(it)
         }.onFailure {
             _bookTitleISBNState.value = UiState.Error("에러입니다~~")
         }.getOrDefault(emptyList())
    }

}