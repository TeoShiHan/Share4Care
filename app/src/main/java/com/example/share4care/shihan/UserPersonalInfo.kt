package com.example.share4care.shihan

import android.os.Parcel
import android.os.Parcelable

data class UserPersonalInfo(
    val status : String?,
    val name: String?,
    val gender: String?,
    val DOB: String?,
    val address: String?,
    val isOKU: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(status)
        parcel.writeString(name)
        parcel.writeString(gender)
        parcel.writeString(DOB)
        parcel.writeString(address)
        parcel.writeString(isOKU)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserPersonalInfo> {
        override fun createFromParcel(parcel: Parcel): UserPersonalInfo {
            return UserPersonalInfo(parcel)
        }

        override fun newArray(size: Int): Array<UserPersonalInfo?> {
            return arrayOfNulls(size)
        }
    }
}




