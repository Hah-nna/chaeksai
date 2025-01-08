package com.jeong.sesac.datamodule.datasource

import com.jeong.sesac.datamodule.dto.Place

interface KakaoMapDataSource {
    suspend fun getLibraryInfo(x : Double, y : Double) : List<Place>
}