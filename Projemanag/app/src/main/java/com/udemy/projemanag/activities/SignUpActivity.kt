package com.udemy.projemanag.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.udemy.projemanag.R
import com.udemy.projemanag.firebase.FirestoreClass
import com.udemy.projemanag.models.User
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        // setup full screen
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        // SetUp Action Bar
        setupActionBar()

        // when click navigation bar
        toolbar_sign_up_activity.setNavigationOnClickListener {
            onBackPressed()
        }

        // START
        // Click event for sign-up button.
        btn_sign_up.setOnClickListener{
            registerUser()
        }
    }

    /**
     * setup action bar
     */
    private fun setupActionBar(){
        setSupportActionBar(toolbar_sign_up_activity)
        val actionBar = supportActionBar
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)

            // setup back arrow
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }
    }

    // START
    /**
     * A function to register a user to our app using the Firebase.
     * For more details visit: https://firebase.google.com/docs/auth/android/custom-auth
     */
    private fun registerUser(){
        val name: String = et_name.text.toString().trim { it <= ' ' }
        val email: String = et_email.text.toString().trim { it <= ' ' }
        val password: String = et_password.text.toString().trim { it <= ' ' }

        if (validateForm(name, email, password)) {
            // SignUpする前ProgressDialogを表示する
            showProgressDialog(resources.getString(R.string.please_wait))
            Log.d("User", "please wait")
            // EmailとPasswordを使用してFirebaseにSignUpする
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        val firebaseUser: FirebaseUser = task.result!!.user!!
                        val registerEmail = firebaseUser.email!!

                        // SignUpが成功の場合、User情報をFirestoreに保存する
                        val user = User(firebaseUser.uid, name, registerEmail)
                        Log.d("User", user.id + user.name + user.email)
                        FirestoreClass().registerUser(this, user)

                    } else {
                        Toast.makeText(this,
                            "Registration Failed!", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
    // END

    // START
    /**
     * A function to call when user register success
     */
    fun userRegisteredSuccess(){
        Log.d("User", "success")
        Toast.makeText(this,
            "Registration Success!", Toast.LENGTH_SHORT).show()
        // Hide Progress Dialog
        hideProgressDialog()
        // SignOut Firebase Auth
        FirebaseAuth.getInstance().signOut()
        // Close SignUpActivity
        finish()
    }

    // START
    /**
     * A function to validate the entries of a new user.
     */
    private fun validateForm(name: String, email: String, password: String): Boolean {
        return when {
            TextUtils.isEmpty(name) -> {
                showErrorSnackBar("Please enter name.")
                false
            }
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