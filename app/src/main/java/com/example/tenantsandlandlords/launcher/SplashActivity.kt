package com.example.tenantsandlandlords.launcher

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tenantsandlandlords.MainActivity
import com.example.tenantsandlandlords.onboarding.OnboardingActivity


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //create sharedpreferences value to check if if its users first time to open the application
        val prefs = getSharedPreferences("prefs", MODE_PRIVATE)
        val firstStart = prefs.getBoolean("firstStart", true)


        //if its the first start of the app
        if (firstStart) {
            //start intent to go to on boarding activity
            val intent = Intent(this, OnboardingActivity::class.java)
            startActivity(intent)
            finish()
        } else
            startActivity(Intent(this, MainActivity::class.java))
    }
}