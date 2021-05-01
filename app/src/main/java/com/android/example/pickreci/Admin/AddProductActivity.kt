package com.android.example.pickreci.Admin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.example.pickreci.Models.RecipeModel
import com.android.example.pickreci.R

class AddProductActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)
        val recipe  = intent.getParcelableExtra<RecipeModel>(AdminActivity.TAG)!!

    }
}