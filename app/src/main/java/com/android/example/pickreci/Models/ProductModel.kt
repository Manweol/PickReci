package com.android.example.pickreci.Models

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize

@Parcelize
@IgnoreExtraProperties
class ProductModel(
    val uid: String? = "",
    val vendorName: String? = "",
    val productImg: String? = "",
    val productName: String? = "",
    val price: Double? = 0.00,
    val weight: String? = "",
    val type: String = "",

) : Parcelable {


}