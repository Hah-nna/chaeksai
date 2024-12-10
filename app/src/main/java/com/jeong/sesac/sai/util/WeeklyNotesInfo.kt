package com.jeong.sesac.sai.util

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WeeklyNotesInfo(
    val id : String = "0",
    val date : String,
    val note_content : String,
    val hint_img: Int = -1,
    val library: String,
    val hints : List<String>
) : Parcelable

// 사진 힌트(사진)
//날짜(쪽지 등록된 날짜)
//도서관명
//힌트 List<String>