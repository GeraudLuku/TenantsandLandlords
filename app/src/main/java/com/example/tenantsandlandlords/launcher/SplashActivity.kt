package com.example.tenantsandlandlords.launcher

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tenantsandlandlords.MainActivity
import com.example.tenantsandlandlords.onboarding.OnboardingActivity
import com.example.tenantsandlandlords.registration.SignupActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class SplashActivity : AppCompatActivity() {

    private var mCurrentUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onStart() {
        super.onStart()

        //get current user
        mCurrentUser = FirebaseAuth.getInstance().currentUser

        //create sharedpreferences value to check if if its users first time to open the application
        val prefs = getSharedPreferences("prefs", MODE_PRIVATE)
        val firstStart = prefs.getBoolean("firstStart", true)


        //if its the first start of the app
        if (firstStart) {
            //start intent to go to on boarding activity
            val intent = Intent(this, OnboardingActivity::class.java)
            startActivity(intent)
            finish()
        } else if (mCurrentUser != null) {//if user is signed in send to another activity //if user is not signed in send to registration section
            //send to main activity first
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            //goto the registration section(Login and SignUp)
            startActivity(Intent(this, SignupActivity::class.java))
            finish()
        }

    }
}