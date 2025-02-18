package com.jeong.sesac.data.dto

import com.google.firebase.firestore.IgnoreExtraProperties
import com.jeong.sesac.feature.model.PlaceInfo
import com.squareup.moshi.JsonClass


@IgnoreExtraProperties
@JsonClass(generateAdapter = true)
data class KakaoMapInfo(
    val documents: List<Place>
)

@JsonClass(generateAdapter = true)
data class Place(
    val id: String,
    val address_name: String,
    val phone: String,
    val place_name: String,
    val place_url: String,
    val x: String,
    val y: String,
    val distance: String,
)

fun Place.toMap(searchRadius: Int): PlaceInfo {
        return PlaceInfo(
            id = id,
            place = place_name,
            address = address_name,
            phone = phone,
            lat = y,
            lng = x,
            placeURL = place_url,
            distance = distance,
            searchRadius = searchRadius
        )
    }
