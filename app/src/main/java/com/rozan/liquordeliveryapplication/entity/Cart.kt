package com.rozan.liquordeliveryapplication.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Cart (
        var ailaId:String?=null,
        var ailaImage:String?=null,
        var ailaPrice:Double?=null,
        var ailaMl:String?=null,
        var ailaName:String?=null,
        var ailaQty:Int?=null,
        var userId:String?=null
        ){
        @PrimaryKey
        var cartId:Int?=null

}