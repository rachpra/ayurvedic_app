package com.rozan.liquordeliveryapplication.entity

data class Users(
        val _id : String? = null,
        var firstName:String?=null,
        var lastName:String?=null,
        var dob:String?=null,
        var username:String?=null,
        var email:String?=null,
        var password:String?=null
) {

}