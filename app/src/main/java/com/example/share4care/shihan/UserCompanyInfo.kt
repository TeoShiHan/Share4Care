package com.example.share4care.shihan

import android.os.Parcel
import android.os.Parcelable

data class UserCompanyInfo(
    val companyName: String?,
    val companyEmail: String?,
    val tel: String?,
    val address: String?,
    val occupation: String?,
    val website: String?,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(companyName)
        parcel.writeString(companyEmail)
        parcel.writeString(tel)
        parcel.writeString(address)
        parcel.writeString(occupation)
        parcel.writeString(website)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserCompanyInfo> {
        override fun createFromParcel(parcel: Parcel): UserCompanyInfo {
            return UserCompanyInfo(parcel)
        }

        override fun newArray(size: Int): Array<UserCompanyInfo?> {
            return arrayOfNulls(size)
        }
    }
}