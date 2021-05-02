package com.android.example.pickreci.Models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class CartProduct(val uid: String? = "", val productUid: String? = "", val quantity: Int? = 0) :
    Parcelable {
}