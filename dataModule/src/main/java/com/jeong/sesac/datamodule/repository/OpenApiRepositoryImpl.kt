package com.jeong.sesac.datamodule.repository

import com.jeong.sesac.datamodule.datasource.OpenAPIDataSourceImpl
import com.jeong.sesac.domain.model.BookInfo
import com.jeong.sesac.domain.repository.IOpenApiRepository

class OpenApiRepositoryImpl(private val openApiDataSource : OpenAPIDataSourceImpl) : IOpenApiRepository {

    override suspend fun getBookInfo(isbn: String): List<BookInfo> {
        val result = openApiDataSource.getBookInfo(isbn).map {
            BookInfo(
                it.page_no,
                it.title,
                it.isbn,
                it.book_img
            )
        }
        return result
    }
}