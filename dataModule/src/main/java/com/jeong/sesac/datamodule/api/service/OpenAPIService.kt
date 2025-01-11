package com.jeong.sesac.datamodule.api.service

import com.jeong.sesac.datamodule.dto.BookInfo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenAPIService {
    @GET("/seoji/SearchApi.do")
    suspend fun getBookInfo(
        @Query("cert_key") cert_key : String = "5c681c482a66e7545fe7969162db961098daf992d6e408f3d53b43217395daf0" ,
        @Query("result_style") result_style : String = "json",
        @Query("page_no") page_no : Int = 1,
        @Query("page_size") page_size : Int = 1,
        @Query("isbn") isbn : String,
        @Query("order_by") order_by : String = "DESC"
    ) : Response<BookInfo>
}