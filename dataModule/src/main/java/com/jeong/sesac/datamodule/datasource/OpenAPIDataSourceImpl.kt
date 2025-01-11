package com.jeong.sesac.datamodule.datasource

import android.util.Log
import com.jeong.sesac.datamodule.api.service.OpenAPIService
import com.jeong.sesac.datamodule.dto.Book

class OpenAPIDataSourceImpl(private val openApiService: OpenAPIService) : OpenAPIDataSource {

    override suspend fun getBookInfo(isbn: String): List<Book> {
    return try {
        val response = openApiService.getBookInfo(isbn = isbn)
        Log.d("response" , "${response.body()?.docs}")

        if (response.isSuccessful && response.body() !== null) {
            response.body()!!.docs
        } else {
            emptyList()
        }
    }catch (e : Exception) {
        throw Error("e : ${e.message}")
    }

    }
}