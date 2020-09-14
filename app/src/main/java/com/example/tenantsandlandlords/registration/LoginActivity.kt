package com.example.tenantsandlandlords.registration

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tenantsandlandlords.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        signupButton.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
            finish()
        }
    }
}