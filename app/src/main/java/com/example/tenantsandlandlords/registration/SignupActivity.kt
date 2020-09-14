package com.example.tenantsandlandlords.registration

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.tenantsandlandlords.R
import kotlinx.android.synthetic.main.activity_signup.*


class SignupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        signinButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        radioGroup.setOnCheckedChangeListener { _, isChecked ->

            when (isChecked) {
                R.id.tenantButton -> Log.d("SignUp", "button pressed 1")
                R.id.landlordButton -> Log.d("SignUp", "button pressed 2")
            }
        }

    }
}