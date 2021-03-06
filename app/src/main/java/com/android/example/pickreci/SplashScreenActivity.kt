package com.android.example.pickreci

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.android.example.pickreci.Admin.ProductActivity

@Suppress("DEPRECATION")
class SplashScreenActivity : AppCompatActivity() {

    private val SplashTime: Long = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler().postDelayed({
            startActivity(Intent (this, MainActivity::class.java))
            //startActivity(Intent (this, ProductActivity::class.java))
            finish()
        }, SplashTime)

    }
}