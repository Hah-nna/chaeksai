package com.jeong.sesac.data.datasource

import android.util.Log
import com.jeong.sesac.data.api.service.KakaoMapService
import com.jeong.sesac.data.dto.toMap
import com.jeong.sesac.feature.model.PlaceInfo

class KakaoMapDataSourceImpl(private val kakaoService: KakaoMapService) : KakaoMapDataSource {
    override suspend fun getLibraryInfo(lng: Double, lat: Double): List<PlaceInfo> {
        return try {
            val response = kakaoService.getLibraryInfo(x = lng, y = lat, radius = 5000)
            Log.d("DataSource", "API Response: ${response.raw().body()}")  // API 요청 URL 확인
            Log.d("DataSource", "Response Code: ${response.code()}")
            if (response.isSuccessful && response.body() !== null) {
                Log.d("DataSource", "Response Body: ${response.body()}")
                response.body()!!.documents.map { it.toMap() }
            } else {
                Log.e("DataSource", "Error Body: ${response.errorBody()?.string()}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("DataSource", "Exception: ${e.message}", e)
            throw Error(e.message)
        }
    }
}