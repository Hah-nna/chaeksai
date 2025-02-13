package com.jeong.sesac.feature.repository

import com.jeong.sesac.feature.model.PlaceInfo

interface IKakaoMapRepository {
    suspend fun getLibraryInfo(lng: Double, lat: Double) : Result<List<PlaceInfo>>
}