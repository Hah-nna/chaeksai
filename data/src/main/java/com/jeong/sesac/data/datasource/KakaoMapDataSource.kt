package com.jeong.sesac.data.datasource

import com.jeong.sesac.feature.model.PlaceInfo

interface KakaoMapDataSource {
    suspend fun getLibraryInfo(x : Double, y : Double) : List<PlaceInfo>
}