package com.jeong.sesac.domain.model

data class PlaceInfo (
    val id : String,
    val address_name : String,
    val phone : String,
    val place_name : String,
    val place_url : String,
    val x : String,
    val y : String
){
    fun toMap() : Map<String, Any> {
        return mapOf(
            "place_name" to place_name,
        )
    }
}