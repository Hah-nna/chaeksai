package com.jeong.sesac.data.api.manager

import com.jeong.sesac.data.api.service.OpenAPIService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


const val OPEN_API_BASE_URL = "https://www.nl.go.kr"

class OpenAPIManager {
    companion object {
        private var retrofitService: OpenAPIService? = null
        fun getInstance(): OpenAPIService {
            if(retrofitService == null) {
                val moshi = Moshi.Builder()
                    .add(KotlinJsonAdapterFactory())
                    .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(OPEN_API_BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()

            retrofitService = retrofit.create(OpenAPIService::class.java)
            }
            return retrofitService!!
        }
    }
}