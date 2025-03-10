package com.jeong.sesac.data.dto

import com.google.firebase.firestore.IgnoreExtraProperties
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@IgnoreExtraProperties
@JsonClass(generateAdapter = true)
data class BookInfo(
    val docs : List<Book>
)

@IgnoreExtraProperties
@JsonClass(generateAdapter = true)
data class Book(
    // 오픈 api에서 오는 데이터 키 이름이 다 대문자임...
    @Json(name = "PAGE_NO")
    val page_no: Int = 1,
    @Json(name = "TITLE")
    val title: String,
    @Json(name = "EA_ISBN")
    val isbn: String,
    @Json(name = "TITLE_URL")
    val book_img: String,
    @Json(name = "AUTHOR")
    val author: String,
    @Json(name = "BOOK_INTRODUCTION")
    val introduction: String,
    @Json(name = "PUBLISHER")
    val publisher: String,
    @Json(name = "FORM")
    val form: String,
    @Json(name = "PUBLISH_PREDATE")
    val publish_date: String,
    @Json(name = "PAGE")
    val page: String
)

