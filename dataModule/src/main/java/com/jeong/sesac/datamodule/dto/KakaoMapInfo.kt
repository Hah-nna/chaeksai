package com.jeong.sesac.datamodule.dto

import com.google.firebase.firestore.IgnoreExtraProperties
import com.squareup.moshi.JsonClass

@IgnoreExtraProperties
@JsonClass(generateAdapter = true)
data class KakaoMapInfo (
    val documents : List<Place>
)

@JsonClass(generateAdapter = true)
data class Place (
    val id : String,
    val address_name : String,
    val phone : String,
    val place_name : String,
    val place_url : String,
    val x : String,
    val y : String
)
