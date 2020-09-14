package com.example.tenantsandlandlords.registration.feature

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.tenantsandlandlords.R
import com.example.tenantsandlandlords.model.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.onurkagan.ksnack_lib.Animations.Fade
import com.onurkagan.ksnack_lib.MinimalKSnack.MinimalKSnack
import com.onurkagan.ksnack_lib.MinimalKSnack.MinimalKSnackStyle
import kotlinx.android.synthetic.main.fragment_signup.*


class SignupFragment : Fragment() {

    private lateinit var mNavController: NavController

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var mAuth: FirebaseAuth
    private val RC_SIGN_IN = 1234

    private var mCategorySelected = false
    private var mCategory = 0  // 0 for tenant 1 for landlord

    private lateinit var mMinimalSnackBar: MinimalKSnack

    private lateinit var mDatabase: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //get navigation controller
        mNavController = findNavController()

        //minimalistic snack bar
        mMinimalSnackBar = MinimalKSnack(this)

        mAuth = FirebaseAuth.getInstance()

        //firebase realtime database reference
        mDatabase = FirebaseDatabase.getInstance().reference

        //init google sign in request
        googleSignupRequest()

        signinButton.setOnClickListener {
            mNavController.navigate(R.id.action_signupFragment_to_loginFragment)
        }

        //get selected category
        radioGroup.setOnCheckedChangeListener { _, isChecked ->

            //if category is selected set the category selected value to true
            when (isChecked) {
                R.id.tenantButton -> {
                    mCategorySelected = true
                    mCategory = 0
                }
                R.id.landlordButton -> {
                    mCategorySelected = true
                    mCategory = 1
                }
            }
        }

        //google sign in clicked
        googleButton.setOnClickListener {
            //if category was selected proceed
            if (mCategorySelected) {
                Log.d("SignUp", "You can create google account $mCategory")
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
            if (mCategorySelected) {
                Log.d("SignUp", "You can create apple account $mCategory")
            }
        }

    }

    //upload new user data into database
    private fun uploadUserInfo(user: FirebaseUser?) {
        //if user is not null proceed
        user?.let {
            //upload user object into database
            mDatabase.child("Users")
                .child(it.uid)
                .setValue(User(it.uid, mCategory))
                .addOnSuccessListener {
                    //if it was successfully uploaded into the database
                    progressView.visibility = View.INVISIBLE
                    Log.d("SignUP", "Successfully uploaded user data...")
                    //send user to main activity
                    mNavController.navigate(R.id.action_signupFragment_to_mainActivity)
                }
                .addOnFailureListener {
                    //if it failed to upload into the database
                    progressView.visibility = View.INVISIBLE
                    showSnackbar("Failed to upload user data...", MinimalKSnackStyle.STYLE_ERROR)
                }
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
        context?.let {
            mGoogleSignInClient = GoogleSignIn.getClient(it, gso)
        }
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
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Google", "signInWithCredential:success")
                    //this is the users information
                    val user = mAuth.currentUser
                    //show snackbar saying sucess
                    showSnackbar("Successful, Uploading data... ", MinimalKSnackStyle.STYLE_SUCCESS)
                    //call function to upload user data in database
                    uploadUserInfo(user)

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
            .setDuration(5000) // you can use for auto close.
            .alignBottom() // bottom align option.
            .show()

    }
}