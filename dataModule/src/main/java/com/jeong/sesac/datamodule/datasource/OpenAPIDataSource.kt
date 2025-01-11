package com.jeong.sesac.datamodule.datasource

import com.jeong.sesac.datamodule.dto.Book

// 국립도서관 isbn 관련 open api
interface OpenAPIDataSource {
    // 스캔한 isbn으로 book title 얻어오기
   suspend fun getBookInfo(isbn : String) : List<Book>
}