package com.jeong.sesac.sai.data

data class RecordItem(
    val title: String,
    val description: String,
    val imageResId: Int,
    val destinationId: Int // 네비게이션 이동 ID
)