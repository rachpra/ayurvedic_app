package com.rozan.liquordeliveryapplication.repository

import android.content.Context
import com.rozan.liquordeliveryapplication.api.APIRequest
import com.rozan.liquordeliveryapplication.api.AilaAPI
import com.rozan.liquordeliveryapplication.api.ServiceBuilder

import com.rozan.liquordeliveryapplication.entity.Aila
import com.rozan.liquordeliveryapplication.response.GetAllAilaResponse

class AilaRepository:APIRequest() {
    val ailaAPI=ServiceBuilder.buildService(AilaAPI::class.java)

    suspend fun getAllAila():GetAllAilaResponse{
        return apiRequest {
            ailaAPI.getAllAila(ServiceBuilder.token!!)
        }

        
      
    }



}