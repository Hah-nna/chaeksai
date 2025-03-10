package com.jeong.sesac.sai.viewmodel

import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeong.sesac.data.repository.LBSRepositoryImpl
import com.jeong.sesac.feature.model.PlaceInfo
import com.jeong.sesac.feature.repository.IKakaoMapRepository
import com.jeong.sesac.sai.model.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

const val SEARCH_PERCENT = 0.9

class KakaoMapViewModel(
    private val kakaoMapRepo: IKakaoMapRepository,
    private val LBSRepo: LBSRepositoryImpl
) : ViewModel() {

    // 현재 위치
    private var _currentLocationState = MutableStateFlow<Location?>(null)
    val currentLocationState = _currentLocationState.asStateFlow()

    // 도서관 정보
    private var _librariesInfoState = MutableStateFlow<UiState<List<PlaceInfo>>>(UiState.Loading)
    val librariesInfoState = _librariesInfoState.asStateFlow()

    // 사용자의 현재 위치
    private var lastSearchLocation: Location? = null

    fun updateLocations() = viewModelScope.launch {
        LBSRepo.findLocation().collectLatest { newLocation ->
            _currentLocationState.value = newLocation

            lastSearchLocation?.let { lastLocation ->
                val movedDistance = calculateDistance(
                    lastLocation.latitude, lastLocation.longitude,
                    newLocation.latitude, newLocation.longitude
                )

                val currentSearchRadius = kakaoMapRepo.getCurrentSearchLocation()
                val searchThreshold = currentSearchRadius * SEARCH_PERCENT

                Log.d("도서관검색", """
                현재 검색 반경: $currentSearchRadius 미터
                이동 거리: $movedDistance 미터
                검색 임계값: $searchThreshold 미터
                새로운 검색 필요: ${movedDistance >= searchThreshold}
            """.trimIndent())

                Log.d("지금 위치", "$currentSearchRadius")

                if(movedDistance >= searchThreshold) {
                    Log.d("도서관검색", "검색반경을 넘었기 때문에 새로운 도서관 검색 시작")
                    getLibrariesInfo(newLocation.longitude, newLocation.latitude)
                    lastSearchLocation = newLocation

                    Log.d("movedDistance", "사용자가 움직인 위치 :${movedDistance} ")
                } else {
                    updateLibraryDistances(newLocation)
                }
            } ?: run {
                // lastSearchLocation이 null인 경우(처음에는 무조건 도서관 목록 검색할 수 있게)
                getLibrariesInfo(newLocation.longitude, newLocation.latitude)
                lastSearchLocation = newLocation
            }
        }
    }

    /**
     * 현재 유저의 위치에서 내의 도서관 정보 받아오기(100m 내에 없다면 500m씩 넓혀짐(최대 10km))
     * */
    fun getLibrariesInfo(lng: Double, lat: Double) = viewModelScope.launch {
        _librariesInfoState.value = UiState.Loading
        Log.d("도서관 정보요청", "경도 $lng 위도 $lat")
        kakaoMapRepo.getLibraryInfo(lng, lat)
            .fold(
                onSuccess = { libraries ->
                    _librariesInfoState.value = UiState.Success(libraries)
                },
                onFailure = { e ->
                    _librariesInfoState.value = UiState.Error(e.message ?: "알수 없는 에러 발생")

                }
            )
    }

    fun updateLibraryDistances(newLocation: Location) {
        val currentLibraries = (_librariesInfoState.value as UiState.Success).data

        val updatedLibraries = currentLibraries.map { library ->
            val newDistance = calculateDistance(
                newLocation.latitude, newLocation.longitude,
                library.lat.toDouble(), library.lng.toDouble())
            library.copy(distance = newDistance.toString())
        }
        _librariesInfoState.value = UiState.Success(updatedLibraries)
    }

}

// 현재 위치에서 얼만큼 거리가 차이나는지 계산하는 함수
private fun calculateDistance(
    currentLat: Double, currentLng: Double,
    clickedLat: Double, clickedLng: Double
): Float {
    val result = FloatArray(1)
    Location.distanceBetween(currentLat, currentLng, clickedLat, clickedLng, result)
    return result[0]
}