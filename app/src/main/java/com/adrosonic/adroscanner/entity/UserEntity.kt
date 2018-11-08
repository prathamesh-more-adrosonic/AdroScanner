package com.adrosonic.adroscanner.entity

import android.os.Parcel
import android.os.Parcelable
import java.util.*


data class UserEntity(
        var name: String? = "",
        var jobTitle: String? = "",
        var company: String? = "",
        var phoneNumber: String? = "",
        var phoneNumberAlt: String? = "",
        var email: String? = "",
        var address: String? = "",
        var website: String? = "",
        var imagePath: String? = "",
        var rotation: Float? = 0f) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(Float::class.java.classLoader) as? Float) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(jobTitle)
        parcel.writeString(company)
        parcel.writeString(phoneNumber)
        parcel.writeString(phoneNumberAlt)
        parcel.writeString(email)
        parcel.writeString(address)
        parcel.writeString(website)
        parcel.writeString(imagePath)
        parcel.writeValue(rotation)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserEntity> {
        override fun createFromParcel(parcel: Parcel): UserEntity {
            return UserEntity(parcel)
        }

        override fun newArray(size: Int): Array<UserEntity?> {
            return arrayOfNulls(size)
        }
    }
}