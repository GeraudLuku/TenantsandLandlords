package com.example.tenantsandlandlords.registration

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.tenantsandlandlords.MainActivity
import com.example.tenantsandlandlords.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.onurkagan.ksnack_lib.Animations.Fade
import com.onurkagan.ksnack_lib.MinimalKSnack.MinimalKSnack
import com.onurkagan.ksnack_lib.MinimalKSnack.MinimalKSnackStyle
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var mAuth: FirebaseAuth

    private lateinit var mMinimalSnackBar: MinimalKSnack

    private val RC_SIGN_IN = 1234


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()

        //init google sign in request
        googleSignupRequest()

        //minimalistic snack bar
        mMinimalSnackBar = MinimalKSnack(this)

        //send user to sign up for a new account
        signupButton.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
            finish()
        }

        //sign in user to app using google authentication
        googleButton.setOnClickListener {
            //sing in user
            signIn()
            overlayView.visibility = View.VISIBLE
        }
    }

    //create google signin request
    fun googleSignupRequest() {
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d("Google", "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.e("Google", "Google sign in failed", e)
                //remove dim background and show snackbar error
                showSnackbar("Error login into your account", MinimalKSnackStyle.STYLE_ERROR)
                // ...
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Google", "signInWithCredential:success")
                    //this is the users information
                    val user = mAuth.currentUser
                    //send user to main activity
                    overlayView.visibility = View.INVISIBLE
                    startActivity(Intent(this, MainActivity::class.java))
                } else {
                    // If sign in fails, display a message to the user.
                    Log.e("Google", "signInWithCredential:failure", task.exception)
                    // failed to sign in into firebase
                    showSnackbar("Firebase sign in failed", MinimalKSnackStyle.STYLE_ERROR)
                }

                // ...
            }
    }

    fun showSnackbar(message: String, style: Int) {

        overlayView.visibility = View.INVISIBLE

        mMinimalSnackBar
            .setMessage(message) // message
            .setStyle(style) // style
            .setAnimation(
                Fade.In.getAnimation(),
                Fade.Out.getAnimation()
            ) // show and hide animations
            .setDuration(5000) // you can use for auto close.
            .alignBottom() // bottom align option.
            .show()

    }
}