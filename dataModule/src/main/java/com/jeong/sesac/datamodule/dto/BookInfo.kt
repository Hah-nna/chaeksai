package com.jeong.sesac.datamodule.dto

import com.google.firebase.firestore.IgnoreExtraProperties
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@IgnoreExtraProperties
@JsonClass(generateAdapter = true)
data class BookInfo(
    // 오픈 api에서 오는 데이터 키 이름이 다 대문자임...
    @Json(name = "PAGE_NO")
    val page_no : Int = 1,
    @Json(name = "TITLE")
    val title : String,
    @Json(name = "EA_ISBN")
    val isbn : String,
    @Json(name = "TITLE_URL")
    val book_img : String
)

