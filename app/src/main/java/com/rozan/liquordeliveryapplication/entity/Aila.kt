package com.rozan.liquordeliveryapplication.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Aila (
    var _id:String?=null,
    var ailaImage:String?=null,
    var ailaPrice:Double?=null,
    var ailaMl:String?=null,
    var ailaName:String?=null,
    var ailaType:String?=null,

        ):Parcelable{
    @PrimaryKey
    var ailaId:Int?=null

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(Double::class.java.classLoader) as? Double,
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
        ailaId = parcel.readValue(Int::class.java.classLoader) as? Int
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(_id)
        parcel.writeString(ailaImage)
        parcel.writeValue(ailaPrice)
        parcel.writeString(ailaMl)
        parcel.writeString(ailaName)
        parcel.writeString(ailaType)
        parcel.writeValue(ailaId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Aila> {
        override fun createFromParcel(parcel: Parcel): Aila {
            return Aila(parcel)
        }

        override fun newArray(size: Int): Array<Aila?> {
            return arrayOfNulls(size)
        }
    }

}