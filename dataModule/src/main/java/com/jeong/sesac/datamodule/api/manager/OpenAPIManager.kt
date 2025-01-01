package com.jeong.sesac.datamodule.api.manager

import com.jeong.sesac.datamodule.api.service.OpenAPIService
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

const val openApiBaseUrl = "https://www.nl.go.kr"

class OpenAPIManager {
    companion object {
        private var retrofitService: OpenAPIService? = null
        fun getInstance(): OpenAPIService {
            if(retrofitService == null) {
            val retrofit = Retrofit.Builder()
                .baseUrl("openApiBaseUrl")
                .addConverterFactory(MoshiConverterFactory.create())
                .build()

            retrofitService = retrofit.create(OpenAPIService::class.java)
            }
            return retrofitService!!
        }
    }
}