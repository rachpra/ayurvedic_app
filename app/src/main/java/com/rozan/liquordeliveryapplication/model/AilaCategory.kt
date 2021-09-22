package com.rozan.liquordeliveryapplication.model

import android.os.Parcel
import android.os.Parcelable

data class AilaCategory(
    val categId:Int?=null,
    val categName:String?=null,
    val categImage:String?=null
):Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(categId)
        parcel.writeString(categName)
        parcel.writeString(categImage)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AilaCategory> {
        override fun createFromParcel(parcel: Parcel): AilaCategory {
            return AilaCategory(parcel)
        }

        override fun newArray(size: Int): Array<AilaCategory?> {
            return arrayOfNulls(size)
        }
    }
}