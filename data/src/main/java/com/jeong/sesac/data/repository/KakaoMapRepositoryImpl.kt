package com.jeong.sesac.data.repository

import android.util.Log
import com.jeong.sesac.data.datasource.KakaoMapDataSourceImpl
import com.jeong.sesac.feature.model.PlaceInfo
import com.jeong.sesac.feature.repository.IKakaoMapRepository

class KakaoMapRepositoryImpl(private val kakakoMapDatasource : KakaoMapDataSourceImpl) : IKakaoMapRepository {
    override suspend fun getLibraryInfo(lng: Double, lat: Double): Result<List<PlaceInfo>> = kakakoMapDatasource.getLibraryInfo(lng, lat)
    override fun getCurrentSearchLocation(): Int {
        return kakakoMapDatasource.getCurrentSearchRadius()
    }
}