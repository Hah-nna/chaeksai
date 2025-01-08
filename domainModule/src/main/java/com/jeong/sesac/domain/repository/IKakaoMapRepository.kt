package com.jeong.sesac.domain.repository

import com.jeong.sesac.domain.model.PlaceInfo

interface IKakaoMapRepository {
    suspend fun getLibraryInfo(x : Double, y : Double) : List<PlaceInfo>
}