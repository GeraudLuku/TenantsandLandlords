package com.example.tenantsandlandlords.registration

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
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
import kotlinx.android.synthetic.main.activity_signup.*


class SignupActivity : AppCompatActivity() {

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private val RC_SIGN_IN = 1234

    private var categorySelected = false
    private var category: Int? = null  // 0 for tenant 1 for landlord

    private lateinit var mMinimalSnackBar: MinimalKSnack

    override fun onStart() {
        super.onStart()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        mMinimalSnackBar = MinimalKSnack(this)

        //init google sign in request
        googleSignupRequest()

        signinButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
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
                signIn()
                //show dim background view
                progressView.visibility = View.VISIBLE
            } else {
                //show error snackbar
                showSnackbar("Choose an account type", MinimalKSnackStyle.STYLE_WARNING)
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

    //upload new user data into database
    private fun uploadUserInfo() {}

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
                progressView.visibility = View.INVISIBLE
                showSnackbar("Google sign in failed", MinimalKSnackStyle.STYLE_ERROR)
                // ...
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Google", "signInWithCredential:success")
                    //this is the users information
                    val user = auth.currentUser
                    //show snackbar saying sucess
                    showSnackbar("Successfully signed up... ", MinimalKSnackStyle.STYLE_SUCCESS)
                    //call function to upload user data in database

                } else {
                    // If sign in fails, display a message to the user.
                    Log.e("Google", "signInWithCredential:failure", task.exception)
                    // failed to sign in into firebase
                    progressView.visibility = View.INVISIBLE
                    showSnackbar("Firebase sign in failed", MinimalKSnackStyle.STYLE_ERROR)
                }

                // ...
            }
    }

    fun showSnackbar(message: String, style: Int) {

        return mMinimalSnackBar
            .setMessage(message) // message
            .setStyle(style) // style
            .setAnimation(
                Fade.In.getAnimation(),
                Fade.Out.getAnimation()
            ) // show and hide animations
            .setDuration(3000) // you can use for auto close.
            .alignBottom() // bottom align option.
            .show()

    }
}