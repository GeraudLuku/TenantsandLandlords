package com.example.tenantsandlandlords.registration

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.tenantsandlandlords.R
import kotlinx.android.synthetic.main.activity_signup.*


class SignupActivity : AppCompatActivity() {

    private var categorySelected = false
    private var category: Int? = null  // 0 for tenant 1 for landlord

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        signinButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }


        //get selected category
        radioGroup.setOnCheckedChangeListener { _, isChecked ->

            //if category is selected set the category selected value to true
            when (isChecked) {
                R.id.tenantButton -> {
                    categorySelected = true
                    category = 0
                }
                R.id.landlordButton -> {
                    categorySelected = true
                    category = 1
                }
            }
        }

        //google sign in clicked
        googleButton.setOnClickListener {
            //if category was selected proceed
            if (categorySelected) {
                Log.d("SignUp", "You can create google account $category")
            }
        }

        //apple sign in clicked
        appleButton.setOnClickListener {
            //if category was selected proceed
            if (categorySelected) {
                Log.d("SignUp", "You can create apple account $category")
            }
        }

    }
}