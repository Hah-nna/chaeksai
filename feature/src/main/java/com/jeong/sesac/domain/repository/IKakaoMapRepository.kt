package com.jeong.sesac.feature.repository

import com.jeong.sesac.feature.model.PlaceInfo

interface IKakaoMapRepository {
    suspend fun getLibraryInfo(x : Double, y : Double) : List<PlaceInfo>
}