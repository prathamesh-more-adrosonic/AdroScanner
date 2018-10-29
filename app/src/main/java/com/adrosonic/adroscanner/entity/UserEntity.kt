package com.adrosonic.adroscanner.entity

import android.os.Parcel
import android.os.Parcelable
import java.util.*


data class UserEntity(
        var name: String? = "",
        var jobTitle: String? = "",
        var company: String? = "",
        var phoneNumber: String? = "",
        var email: String? = "",
        var address: String? = "",
        var website: String? = "",
        var image: ByteArray? = byteArrayOf()) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.createByteArray()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(jobTitle)
        parcel.writeString(company)
        parcel.writeString(phoneNumber)
        parcel.writeString(email)
        parcel.writeString(address)
        parcel.writeString(website)
        parcel.writeByteArray(image)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserEntity

        if (name != other.name) return false
        if (jobTitle != other.jobTitle) return false
        if (company != other.company) return false
        if (phoneNumber != other.phoneNumber) return false
        if (email != other.email) return false
        if (address != other.address) return false
        if (website != other.website) return false
        if (!Arrays.equals(image, other.image)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name?.hashCode() ?: 0
        result = 31 * result + (jobTitle?.hashCode() ?: 0)
        result = 31 * result + (company?.hashCode() ?: 0)
        result = 31 * result + (phoneNumber?.hashCode() ?: 0)
        result = 31 * result + (email?.hashCode() ?: 0)
        result = 31 * result + (address?.hashCode() ?: 0)
        result = 31 * result + (website?.hashCode() ?: 0)
        result = 31 * result + (image?.let { Arrays.hashCode(it) } ?: 0)
        return result
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