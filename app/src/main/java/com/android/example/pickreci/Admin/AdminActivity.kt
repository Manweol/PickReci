package com.android.example.pickreci.Admin

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.android.example.pickreci.InitialScreen.InitialScreen
import com.android.example.pickreci.LoginScreen.LoginScreenActivity
import com.android.example.pickreci.R
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_admin2.*

class AdminActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin2)




        var recipes = findViewById<Button>(R.id.button)
        var products = findViewById<Button>(R.id.button5)



        recipes.setOnClickListener {
            startActivity(Intent(this, RecipeActivity::class.java))
        }

        products.setOnClickListener {
            startActivity(Intent(this, ProductActivity::class.java))
        }





    }
}