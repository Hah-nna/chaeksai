package com.jeong.sesac.data.datasource

import android.util.Log
import com.jeong.sesac.data.api.service.OpenAPIService

class OpenAPIDataSourceImpl(private val openApiService: OpenAPIService) : OpenAPIDataSource {
    override suspend fun getBookInfo(isbn: String): Result<String> {
        return runCatching {
            val response = openApiService.getBookInfo(isbn = isbn)
            Log.d("response", "${response.body()?.docs}")

            if (!response.isSuccessful) {
                throw Exception("에러에러: ${response.message()}")
            }

            val bookInfo = response.body()?.docs?.firstOrNull()
                ?: throw Exception("ISBN에 해당하는 책이 없습니다...: $isbn")
            bookInfo.title
        }
    }
}