package com.example.socialmediaapp.Model

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable


data class User(
    val username : String = "",
    val userid : String = "",
    val profileImageUrl : String = "",
    var followers : String = "",
    var follows : String = ""
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(username)
        parcel.writeString(userid)
        parcel.writeString(profileImageUrl)
        parcel.writeString(followers)
        parcel.writeString(follows)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}