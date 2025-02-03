package com.jeong.sesac.data.api.service

import com.jeong.sesac.data.dto.KakaoMapInfo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface KakaoMapService {
    @GET("/v2/local/search/keyword.json")
    suspend fun getLibraryInfo(
        @Header("Authorization") Authorization: String = "KakaoAK 2b8d38f4aba81cdf02cdd642a31ee1d0",
        @Query("x") x : Double,
        @Query("y") y : Double,
        @Query("radius") radius : Int,
        @Query("query") query : String = "도서관",
    ) : Response<KakaoMapInfo>
}