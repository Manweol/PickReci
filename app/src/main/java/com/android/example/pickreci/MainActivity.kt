package com.android.example.pickreci

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    lateinit var navView: NavigationView
    lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        setSupportActionBar(toolbar)

        navController = Navigation.findNavController(this, R.id.fragment)

        navView = findViewById(R.id.nav_view)
        NavigationUI.setupWithNavController(nav_view,navController)
        NavigationUI.setupActionBarWithNavController(this,navController,drawer_layout)

    }


}