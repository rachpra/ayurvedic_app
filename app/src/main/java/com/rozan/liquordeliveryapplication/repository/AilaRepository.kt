package com.rozan.liquordeliveryapplication.repository

import android.content.Context
import com.rozan.liquordeliveryapplication.api.APIRequest
import com.rozan.liquordeliveryapplication.api.AilaAPI
import com.rozan.liquordeliveryapplication.api.ServiceBuilder
import com.rozan.liquordeliveryapplication.db.AilaDB
import com.rozan.liquordeliveryapplication.entity.Aila
import com.rozan.liquordeliveryapplication.response.GetAllAilaResponse

class AilaRepository:APIRequest() {
    val ailaAPI=ServiceBuilder.buildService(AilaAPI::class.java)

    suspend fun getAllAila(context: Context):MutableList<Aila>{
        val response= apiRequest {
            ailaAPI.getAllAila(ServiceBuilder.token!!)
        }

        val lstAila:MutableList<Aila> = response.data!!
        AilaDB.getInstance(context).clearAllTables()
        AilaDB.getInstance(context).getAilaDAO().insertAila(lstAila)

        val allAila:MutableList<Aila> = AilaDB.getInstance(context).getAilaDAO().getAila()
        return allAila
    }
    suspend fun getAilaByCateg(ailaType:String):GetAllAilaResponse{
        return apiRequest {
            ailaAPI.getAilaByCateg(ServiceBuilder.token!!,ailaType)
        }
    }


}