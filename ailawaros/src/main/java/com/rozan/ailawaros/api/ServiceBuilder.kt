package com.rozan.liquordeliveryapplication.api

import com.rozan.liquordeliveryapplication.entity.Users
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceBuilder {
    private const val BASE_URL =
            "http://10.0.2.2:90/"
    var token: String? = null

    private val okHttp = OkHttpClient.Builder()
    private val retrofitBuilder = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttp.build())
    //Create retrofit instance
    private val retrofit = retrofitBuilder.build()
    //Generic function
    fun <T> buildService(serviceType: Class<T>): T {
        return retrofit.create(serviceType)
    }
    fun loadImagePath(): String {
        return BASE_URL
    }
}