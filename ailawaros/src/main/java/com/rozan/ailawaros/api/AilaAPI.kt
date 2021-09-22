package com.rozan.liquordeliveryapplication.api

import com.rozan.liquordeliveryapplication.response.GetAllAilaResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface AilaAPI {

    @GET("aila/all")
    suspend fun getAllAila(
            @Header("Authorization") token:String
    ):Response<GetAllAilaResponse>


}