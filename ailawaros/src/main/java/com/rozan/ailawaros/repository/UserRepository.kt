package com.rozan.liquordeliveryapplication.repository

import com.rozan.liquordeliveryapplication.api.APIRequest
import com.rozan.liquordeliveryapplication.api.ServiceBuilder
import com.rozan.liquordeliveryapplication.api.UserAPI
import com.rozan.liquordeliveryapplication.entity.Users
import com.rozan.liquordeliveryapplication.response.LoginResponse


class UserRepository:APIRequest() {
    val api=ServiceBuilder.buildService(UserAPI::class.java)


    suspend fun checkUser(username: String, password: String): LoginResponse {
        return apiRequest {
            api.checkUser(username, password)
        }
    }

}