package com.android.example.pickreci.Models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Product(
    val uid: String? = "",
    val productImg: String? = "",
    val productName: String? = "",
    val price: Double? = 0.0,
    val weight: String? = "",
) : Parcelable {
}