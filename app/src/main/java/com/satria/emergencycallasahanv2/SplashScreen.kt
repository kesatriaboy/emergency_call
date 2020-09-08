package com.satria.emergencycallasahanv2

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity


class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler().postDelayed(Runnable {
            startActivity(
                Intent(
                    this@SplashScreen,
                    MainActivity::class.java
                )
            )
            finish()
        }, 2000)
    }
}