package com.jeong.sesac.datamodule.datasource

// 국립도서관 isbn 관련 open api
interface OpenAPIDataSource {
    // 스캔한 isbn으로 book title 얻어오기
    fun getBookInfo()
}