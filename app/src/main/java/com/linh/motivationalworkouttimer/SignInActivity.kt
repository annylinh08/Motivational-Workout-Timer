package com.linh.motivationalworkouttimer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        mAuth = FirebaseAuth.getInstance()

        buttonSigin1.setOnClickListener{
            startSignin()
        }

        forgotPassword.setOnClickListener{
            val intent = Intent(applicationContext, ResetPassActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun startSignin() {
        val email: String = editTextEmail1.text.toString()
        val password: String = editTextPassword1.text.toString()
        if (email.isNullOrEmpty() || password.isNullOrEmpty())
        {
            Toast.makeText(this, "Invalid login", Toast.LENGTH_LONG).show()
        }
        else{
            mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener{ task ->
                    if(task.isSuccessful){
                        Toast.makeText(this, "Successfully signed in", Toast.LENGTH_LONG).show()
                        val intent = Intent(applicationContext, ExercisesListActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    else{
                        Toast.makeText(this, "Error Message: " +task.exception!!.message.toString(), Toast.LENGTH_LONG).show()
                    }
                }

        }
    }
}