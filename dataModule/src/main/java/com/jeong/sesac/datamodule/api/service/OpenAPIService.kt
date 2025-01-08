package com.jeong.sesac.datamodule.api.service

import com.jeong.sesac.datamodule.dto.BookInfo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * FireBaseService에서
 * 어떤 http 메소드를 사용할지, 어떤 파라미터가 필요한지, 어떤 응답을 받을지 선언
 * 아직
 * */

interface OpenAPIService {
    @GET("/seoji/SearchApi.do")
    suspend fun getUserInfo(
        @Query("cert_key") cert_key : String,
        @Query("result_style") result_style : String = "json",
        @Query("page_no") page_no : Int = 1,
        @Query("page_size") page_size : Int = 1,
        @Query("isbn") isbn : String,
        @Query("title") title : String,
        @Query("order_by") order_by : String = "DESC"
    ) : Response<BookInfo>
}