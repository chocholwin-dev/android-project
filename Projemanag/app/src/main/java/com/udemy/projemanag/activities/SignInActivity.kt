package com.udemy.projemanag.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.udemy.projemanag.R
import com.udemy.projemanag.firebase.FirestoreClass
import com.udemy.projemanag.models.User
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : BaseActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        // setup full screen
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // SetUp Action Bar
        setupActionBar()

        // when click navigation bar
        toolbar_sign_in_activity.setNavigationOnClickListener {
            onBackPressed()
        }

        // START
        // Click event for sign-in button.
        btn_sign_in.setOnClickListener {
            signInRegisteredUser()
        }
    }

    /**
     * setup action bar
     */
    private fun setupActionBar(){
        setSupportActionBar(toolbar_sign_in_activity)
        val actionBar = supportActionBar
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)

            // setup back arrow
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }
    }

    /**
     * A function for Sign-In using the registered user using the email and password.
     */
    private fun signInRegisteredUser() {
        // Here we get the text from editText and trim the space
        val email: String = et_email_sign_in.text.toString().trim { it <= ' ' }
        val password: String = et_password_sign_in.text.toString().trim { it <= ' ' }

        if (validateForm(email, password)) {
            // Show the progress dialog.
            showProgressDialog(resources.getString(R.string.please_wait))

            // Sign-In using FirebaseAuth
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Calling the FirestoreClass signInUser function to get the data of user from database.
                        //FirestoreClass().signInUser(this@SignInActivity)
                        Log.d("Sign in", "SignInWithEmail:Success")
                        val user = auth.currentUser
                        FirestoreClass().loadUserData(this)
                    } else {
                        // // If sign in fails, display a mess age to the user.
                        Log.w("Sign in", "createUserWithEmail:failure", task.exception)
                        Toast.makeText(
                            this@SignInActivity,
                            task.exception!!.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }
    }

    /**
     * A function to call when user sign in success
     */
    fun signInSuccess(user: User){
        Toast.makeText(this,
            "Sign In Success!", Toast.LENGTH_SHORT).show()
        // Hide Progress Dialog
        hideProgressDialog()
        // MainActivityへ移動する
        startActivity(Intent(this, MainActivity::class.java))
        // Close SignUpActivity
        finish()
    }

    // START
    /**
     * A function to validate the entries of a new user.
     */
    private fun validateForm(email: String, password: String): Boolean {
        return when {
            TextUtils.isEmpty(email) -> {
                showErrorSnackBar("Please enter email.")
                false
            }
            TextUtils.isEmpty(password) -> {
                showErrorSnackBar("Please enter password.")
                false
            }
            else -> {
                true
            }
        }
    }
    // END
}