package com.jeong.sesac.data.api.manager

import com.jeong.sesac.data.api.service.KakaoMapService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


const val KAKAO_MAP_API_BASE_URL = "https://dapi.kakao.com"

class KakaoMapManager{
    companion object {
        private var retrofitService: KakaoMapService? = null
        fun getInstance() : KakaoMapService {
            if(retrofitService == null) {
                val moshi = Moshi.Builder()
                    .add(KotlinJsonAdapterFactory())
                    .build()

                val retrofit = Retrofit.Builder()
                    .baseUrl(KAKAO_MAP_API_BASE_URL)
                    .addConverterFactory(MoshiConverterFactory.create(moshi))
                    .build()
                retrofitService = retrofit.create(KakaoMapService::class.java)
            }
            return retrofitService!!
        }
    }
}