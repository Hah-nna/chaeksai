package com.jeong.sesac.data.repository

import android.util.Log
import com.jeong.sesac.data.datasource.KakaoMapDataSourceImpl
import com.jeong.sesac.feature.model.PlaceInfo
import com.jeong.sesac.feature.repository.IKakaoMapRepository

class KakaoMapRepositoryImpl(private val kakakoMapDatasource : KakaoMapDataSourceImpl) : IKakaoMapRepository {

    override suspend fun getLibraryInfo(lng: Double, lat: Double): List<PlaceInfo> {
        val result = kakakoMapDatasource.getLibraryInfo(lng, lat).map {
        PlaceInfo(
            it.id,
            it.address,
            it.phone,
            it.place,
            it.placeURL,
            it.lat,
            it.lng
        )
        }
        Log.d("Repository", "Got libraries: $result")
    return result
    }
}