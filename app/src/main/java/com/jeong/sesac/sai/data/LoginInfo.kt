package com.jeong.sesac.sai.data

data class LoginInfo(
    val companyName: String,  // 로그인 회사명
    val signInDate: String,   // 로그인 날짜
    val isConnected: Boolean  // 연결 상태
)