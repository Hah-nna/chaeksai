package com.jeong.sesac.data.datasource

import android.util.Log
import com.jeong.sesac.data.api.service.KakaoMapService
import com.jeong.sesac.data.dto.toMap
import com.jeong.sesac.feature.model.PlaceInfo

const val INITIAL_SEARCH_RADIUS = 300
const val RADIUS_INCREMENT = 500

class KakaoMapDataSourceImpl(private val kakaoService: KakaoMapService) : KakaoMapDataSource {
    var lastSearchRadius = INITIAL_SEARCH_RADIUS

    override suspend fun getLibraryInfo(lng: Double, lat: Double): Result<List<PlaceInfo>> {
        var currentRadius = INITIAL_SEARCH_RADIUS
        Log.d("datasource", "처음 currentRadius: ${currentRadius}")

        return runCatching {
            var response = kakaoService.getLibraryInfo(x = lng, y = lat, radius = currentRadius)

            Log.d(
                "DataSource",
                "Response Code: ${response.code()}, response body : ${response.body()}"
            )

            while (response.isSuccessful && response.body()!!.documents.isEmpty()) {
                currentRadius += RADIUS_INCREMENT

                response = kakaoService.getLibraryInfo(x = lng, y = lat, radius = currentRadius)
            }

            if (response.isSuccessful && response.body() != null) {
                lastSearchRadius = currentRadius
                Log.d("DataSource", "Success: ${response.body()}")
                Log.d("datasource", "도서관 검색 범위: ${currentRadius}")
                response.body()!!.documents.map { it.toMap(currentRadius) }
            } else {
                throw Exception("도서관정보 가져오기 실패: ${response.errorBody()?.string()}")
            }
        }
    }

   override fun getCurrentSearchRadius() = lastSearchRadius
}