package com.jeong.sesac.sai.util

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WeeklyNotesInfo(
    val id : String = "0",
    val date : String,
    val note_content : String,
    val hint_img: Int,
    val library: String,
    val hints : List<String>
) : Parcelable
