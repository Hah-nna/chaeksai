package com.jeong.sesac.sai.viewmodel

import androidx.lifecycle.ViewModel
import com.jeong.sesac.domain.model.Review
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MyPageViewModel() : ViewModel() {

    private var _reviewList = MutableStateFlow<List<Review>>(emptyList())
    val reviewList get() = _reviewList.asStateFlow()

    fun getReviews() {
        // myPageRepo.getReviewList()
    }

    fun getUserInfo() {

    }

    fun updateProfile() {

    }

    fun updateNickName(){

    }

    fun follow() {

    }

    fun unfollow() {

    }
}