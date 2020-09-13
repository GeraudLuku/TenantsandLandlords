package com.example.tenantsandlandlords.launcher

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tenantsandlandlords.MainActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //start intent to go to main activity
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}