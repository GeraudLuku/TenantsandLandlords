package com.example.tenantsandlandlords

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tenantsandlandlords.registration.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //logout user
        logOutButton.setOnClickListener {
            //logout user account on device from firebase
            FirebaseAuth.getInstance()
                .signOut()
            //send user to login activity
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}