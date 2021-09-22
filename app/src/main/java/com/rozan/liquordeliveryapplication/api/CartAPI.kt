package com.rozan.liquordeliveryapplication.api

import com.rozan.liquordeliveryapplication.entity.Carts
import com.rozan.liquordeliveryapplication.response.CartResponse
import retrofit2.Response
import retrofit2.http.*

interface CartAPI {

    @POST("add/cart2/{id}")
    suspend fun addToCart(
            @Header("Authorization") token:String,
            @Path("id") id:String,
            @Body cart: Carts
    ):Response<CartResponse>

    @GET("cart/all")
    suspend fun getCart(
            @Header("Authorization") token: String
    ):Response<CartResponse>

    @DELETE("cart/delete/{id}")
    suspend fun deleteCart(
            @Header("Authorization") token: String,
            @Path("id") id:String
    ):Response<CartResponse>
}