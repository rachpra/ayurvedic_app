package com.rozan.liquordeliveryapplication.response

import com.rozan.liquordeliveryapplication.entity.Aila

data class GetAllAilaResponse(
        val data:MutableList<Aila>?=null
)