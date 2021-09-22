package com.rozan.liquordeliveryapplication.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(

        var firstName:String?=null,
        var lastName:String?=null,
        var dob:String?=null,
        var username:String?=null,
        var email:String?=null,
        var password:String?=null
) {
    @PrimaryKey(autoGenerate = true)
    var userId:Int=0
}