package com.jeong.sesac.data.repository

import com.jeong.sesac.data.datasource.OpenAPIDataSource
import com.jeong.sesac.feature.repository.IOpenApiRepository

class OpenApiRepositoryImpl(private val openApiDataSource : OpenAPIDataSource) :
    IOpenApiRepository {

    override suspend fun getBookInfo(isbn: String): Result<String> {
        return openApiDataSource.getBookInfo(isbn)
    }
}