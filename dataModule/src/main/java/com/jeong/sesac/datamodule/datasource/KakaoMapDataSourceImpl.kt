package com.jeong.sesac.datamodule.datasource

import android.util.Log
import com.jeong.sesac.datamodule.api.service.KakaoMapService
import com.jeong.sesac.datamodule.dto.Place

class KakaoMapDataSourceImpl(private val kakaoService: KakaoMapService) : KakaoMapDataSource {
    override suspend fun getLibraryInfo(x: Double, y: Double): List<Place> {
        return try {
            val response = kakaoService.getLibraryInfo(x = x, y = y, radius = 5000)
            Log.d("DataSource", "API Response: ${response.raw().request.url}")  // API 요청 URL 확인
            Log.d("DataSource", "Response Code: ${response.code()}")
            if (response.isSuccessful && response.body() !== null) {
                Log.d("DataSource", "Response Body: ${response.body()}")
                response.body()!!.documents
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