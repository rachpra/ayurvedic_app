package com.rozan.liquordeliveryapplication.api

import com.rozan.liquordeliveryapplication.entity.Users
import com.rozan.liquordeliveryapplication.response.LoginResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface UserAPI {

    @FormUrlEncoded
    @POST("user/login")
    suspend fun checkUser(
        @Field("username") username:String,
        @Field("password") password:String
    ):Response<LoginResponse>

}