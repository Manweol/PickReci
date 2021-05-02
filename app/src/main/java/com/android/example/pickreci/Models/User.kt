package com.android.example.pickreci.Models

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize

@Parcelize
@IgnoreExtraProperties
class User(
    val uid: String? = "",
    val name: String = "",
    val email: String? = "",
    val number: Int? = 0,
    val address: String? = "",
    val imageURL: String? = ""
): Parcelable {
}