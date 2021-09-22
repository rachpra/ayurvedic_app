package com.rozan.liquordeliveryapplication.response

import com.rozan.liquordeliveryapplication.entity.Users

data class LoginResponse (
    val success:Boolean?=null,
    val message:String?=null,
    val token:String?=null,
    val userId:String?=null,
    val data:MutableList<Users>?=null
        ){

}