package com.jeong.sesac.sai.data

data class Review(
    val profileImageResId: Int, // 프로필 이미지 리소스 ID
    val nickname: String,       // 닉네임
    val content: String         // 리뷰 내용
)