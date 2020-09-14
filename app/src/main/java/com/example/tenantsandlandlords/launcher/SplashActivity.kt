package com.example.tenantsandlandlords.launcher

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tenantsandlandlords.MainActivity
import com.example.tenantsandlandlords.onboarding.OnboardingActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //start intent to go to main activity
        val intent = Intent(this, OnboardingActivity::class.java)
        startActivity(intent)
    }
}