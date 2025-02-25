package com.jeong.sesac.data.repository

import com.jeong.sesac.data.datasource.OpenAPIDataSourceImpl
import com.jeong.sesac.feature.model.BookInfo
import com.jeong.sesac.feature.repository.IOpenApiRepository

class OpenApiRepositoryImpl(private val openApiDataSource : OpenAPIDataSourceImpl) :
    IOpenApiRepository {

    override suspend fun getBookInfo(isbn: String): Result<String> {
        return openApiDataSource.getBookInfo(isbn)
    }
}