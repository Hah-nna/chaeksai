package com.jeong.sesac.datamodule.repository

import android.util.Log
import com.jeong.sesac.datamodule.datasource.KakaoMapDataSourceImpl
import com.jeong.sesac.domain.model.PlaceInfo
import com.jeong.sesac.domain.repository.IKakaoMapRepository

class KakaoMapRepositoryImpl(private val kakakoMapDatasource : KakaoMapDataSourceImpl) : IKakaoMapRepository {

    override suspend fun getLibraryInfo(x: Double, y: Double): List<PlaceInfo> {
        val result = kakakoMapDatasource.getLibraryInfo(x,y).map {
        PlaceInfo(
            it.id,
            it.address_name,
            it.phone,
            it.place_name,
            it.place_url,
            it.x,
            it.y
        )
        }
        Log.d("Repository", "Got libraries: $result")
    return result
    }
}