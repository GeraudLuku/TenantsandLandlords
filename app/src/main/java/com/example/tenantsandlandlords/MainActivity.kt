package com.example.tenantsandlandlords

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tenantsandlandlords.registration.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.onurkagan.ksnack_lib.Animations.Fade
import com.onurkagan.ksnack_lib.MinimalKSnack.MinimalKSnack
import com.onurkagan.ksnack_lib.MinimalKSnack.MinimalKSnackStyle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var mMinimalSnackBar: MinimalKSnack

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mMinimalSnackBar = MinimalKSnack(this)
        mMinimalSnackBar
            .setMessage("Successfully Logged into app...") // message
            .setStyle(MinimalKSnackStyle.STYLE_SUCCESS) // style
            .setAnimation(
                Fade.In.getAnimation(),
                Fade.Out.getAnimation()
            ) // show and hide animations
            .setDuration(5000) // you can use for auto close.
            .alignBottom() // bottom align option.
            .show()

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