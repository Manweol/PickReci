package com.android.example.pickreci.Models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class Ingredient(val ingredient: String? = "", val recipeUid: String? = "") {
}