package com.android.example.pickreci.Models

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize

@Parcelize
@IgnoreExtraProperties
class RecipeModel (val uid: String? = "",
                   val title: String? = "",
                   val type: String = "",
                   val imageURL: String? = "" ):Parcelable {
}