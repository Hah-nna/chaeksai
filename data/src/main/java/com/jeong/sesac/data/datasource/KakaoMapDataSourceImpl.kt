package com.jeong.sesac.data.datasource

import android.util.Log
import com.jeong.sesac.data.api.service.KakaoMapService
import com.jeong.sesac.data.dto.toMap
import com.jeong.sesac.feature.model.PlaceInfo

class KakaoMapDataSourceImpl(private val kakaoService: KakaoMapService) : KakaoMapDataSource {
    override suspend fun getLibraryInfo(lng: Double, lat: Double): Result<List<PlaceInfo>> {
        var currentRadius = 0
        return runCatching {
            var response = kakaoService.getLibraryInfo(x = lng, y = lat, radius = currentRadius + 100)

            Log.d("DataSource", "Response Code: ${response.code()}, response body : ${response.body()}")

            while(response.isSuccessful && response.body()!!.documents.isEmpty()) {
                currentRadius += 500
                Log.d("datasource", "currentRadius: ${currentRadius}")

                response = kakaoService.getLibraryInfo(x = lng, y = lat, radius = currentRadius)
            }


            if (response.isSuccessful && response.body() != null) {
                Log.d("DataSource", "Success: ${response.body()}")
                currentRadius = 0
                response.body()!!.documents.map { it.toMap() }
            } else {
                throw Exception("도서관정보 가져오기 실패: ${response.errorBody()?.string()}")
            }
        }
    }
}