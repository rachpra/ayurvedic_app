package com.rozan.liquordeliveryapplication.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "favorite")
data class Favorites(
        val ailaId:Int?=null,
        val ailaImage:String?=null,
        val ailaName:String?=null,
        val ailaPrice:Double?=null,
        val ailaMl:String?=null,
        val ailaType:String?=null,
) {
    @PrimaryKey(autoGenerate = true)
    var favorite_id:Int=0
}