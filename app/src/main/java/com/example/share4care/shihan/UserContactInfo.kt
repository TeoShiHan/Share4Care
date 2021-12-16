package com.example.share4care.shihan

import android.os.Parcel
import android.os.Parcelable

data class UserContactInfo(var phoneNumber: String?, var email: String?, var imglink: String) : Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        ""
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(phoneNumber)
        parcel.writeString(email)
        parcel.writeString(imglink)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserContactInfo> {
        override fun createFromParcel(parcel: Parcel): UserContactInfo {
            return UserContactInfo(parcel)
        }

        override fun newArray(size: Int): Array<UserContactInfo?> {
            return arrayOfNulls(size)
        }
    }

}