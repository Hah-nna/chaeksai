package com.jeong.sesac.data.datasource

import com.jeong.sesac.feature.model.PlaceInfo

interface KakaoMapDataSource {
    suspend fun getLibraryInfo(lng: Double, lat: Double) : Result<List<PlaceInfo>>
    fun getCurrentSearchRadius(): Int
}