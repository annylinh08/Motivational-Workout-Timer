package com.example.motivationalworkouttimer

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_reset_pass.*
import kotlinx.android.synthetic.main.activity_sign_up.*

class ResetPassActivity : AppCompatActivity() {
    private lateinit var mAuth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_pass)

        mAuth = FirebaseAuth.getInstance()
        buttonReset.setOnClickListener{
           startReset()
        }
    }

    private fun startReset() {
        val email: String = editTextEmailReset.text.toString()
        if (email.isNullOrEmpty()){
            Toast.makeText(this, "Email Address is not provided", Toast.LENGTH_LONG).show()
        }
        else{
            mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = mAuth.currentUser
                    Toast.makeText(this, "Reset password link is mailed. Please check your email", Toast.LENGTH_LONG).show()
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else{
                    Toast.makeText(this, "Password reset could not be sent", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

}