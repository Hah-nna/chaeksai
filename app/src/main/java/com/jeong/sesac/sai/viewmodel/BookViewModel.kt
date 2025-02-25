package com.jeong.sesac.sai.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeong.sesac.data.repository.BookRepositoryImpl
import com.jeong.sesac.data.repository.OpenApiRepositoryImpl
import com.jeong.sesac.feature.model.BookInfo
import com.jeong.sesac.feature.model.BookReviewWithUser
import com.jeong.sesac.sai.model.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BookViewModel(
    private val bookRepo: BookRepositoryImpl,
    private val openApiRepo: OpenApiRepositoryImpl
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<Any>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _bookTitle = MutableStateFlow<UiState<String>?>(null)
    val bookTitle = _bookTitle.asStateFlow()

    private var _bookListState = MutableStateFlow<UiState<List<BookInfo>>>(UiState.Loading)
    val bookListState: StateFlow<UiState<List<BookInfo>>> = _bookListState.asStateFlow()

    private var _bookState = MutableStateFlow<UiState<BookInfo>>(UiState.Loading)
    val bookState = _bookState.asStateFlow()

    private var _createReviewState = MutableStateFlow<UiState<Boolean>>(UiState.Loading)
    val createReviewState = _createReviewState.asStateFlow()

    private var _getReviewsState = MutableStateFlow<UiState<List<BookReviewWithUser>>>(UiState.Loading)
    val getReviewsState = _getReviewsState.asStateFlow()

   private var _updateReviewState = MutableStateFlow<UiState<Boolean>>(UiState.Loading)
   val updateReviewState = _updateReviewState.asStateFlow()

    private var _deleteReviewState = MutableStateFlow<UiState<Boolean>>(UiState.Loading)
    val deleteReviewState = _deleteReviewState.asStateFlow()

    fun createBook(isbn: String, userId: String, library: String) = viewModelScope.launch {
        _uiState.value = UiState.Loading
        bookRepo.createBook(isbn, userId, library)
            .onSuccess {
                _uiState.value = UiState.Success(it)
            }.onFailure {
                throw Exception("책 등록 실패 ${it.message}, ${it.cause}")
            }
    }

    fun getBookTitle(isbn: String) = viewModelScope.launch {
        _bookTitle.value = UiState.Loading
        openApiRepo.getBookInfo(isbn)
            .onSuccess {
                _bookTitle.value = UiState.Success(it)
            }.onFailure { e ->
                _bookTitle.value = UiState.Error(e.message.toString())
            }
    }

    fun getBookList(library: String) = viewModelScope.launch {
        _bookListState.value = UiState.Loading
        bookRepo.getBookList(library)
            .onSuccess { bookList ->
                _bookListState.value = UiState.Success(bookList)
            }.onFailure { e ->
                _bookListState.value = UiState.Error(e.message.toString())
            }
    }

    fun getBook(library: String, bookId: String) = viewModelScope.launch {
        _bookState.value = UiState.Loading
        bookRepo.getBook(library, bookId)
            .onSuccess {
                _bookState.value = UiState.Success(it)
            }
            .onFailure {
                _bookState.value = UiState.Error(it.message.toString())
            }
    }


    fun createBookReview(userId: String, content: String, bookId: String, score: Float, library: String) = viewModelScope.launch {
        _createReviewState.value = UiState.Loading
        bookRepo.createBookReview(userId, content, bookId, score, library)
            .onSuccess {
                _createReviewState.value = UiState.Success(it)
                getBookReviews(bookId)
            }
            .onFailure {
                _createReviewState.value = UiState.Error(it.message.toString())
            }
    }

    fun getBookReviews(bookId: String) = viewModelScope.launch {
        _getReviewsState.value = UiState.Loading
        bookRepo.getBookReviews(bookId)
            .onSuccess {
                _getReviewsState.value = UiState.Success(it.reversed())
            }.onFailure {
                _getReviewsState.value = UiState.Error(it.message.toString())
            }
    }

    fun updateBookReview(bookId: String, reviewId: String, content: String, score: Float) = viewModelScope.launch {
        _updateReviewState.value = UiState.Loading
        bookRepo.updateBookReview(bookId, reviewId, content, score)
            .onSuccess {
                _updateReviewState.value = UiState.Success(it)
            }.onFailure {
                _updateReviewState.value = UiState.Error(it.message.toString())
            }
    }

    fun deleteBookReview(bookId: String, reviewId: String) = viewModelScope.launch {
        _deleteReviewState.value = UiState.Loading
        bookRepo.deleteBookReview(bookId, reviewId)
            .onSuccess {
                getBookReviews(bookId)
                _deleteReviewState.value = UiState.Success(it)
            }.onFailure {
                _deleteReviewState.value = UiState.Error(it.message.toString())
            }
    }

}