package com.example.motivationalworkouttimer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_sign_up.*


class SignUpActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var refUsers: DatabaseReference
    private var firebaseUserID: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        mAuth = FirebaseAuth.getInstance()
        buttonSignup1.setOnClickListener {
            startSignUp()
        }
    }

    private fun startSignUp() {
        val username: String = editTextName.text.toString()
        val email: String = editTextEmail.text.toString()
        val password: String = editTextPassword.text.toString()

        if (username.isNullOrEmpty() || email.isNullOrEmpty() || password.isNullOrEmpty())
        {
            Toast.makeText(this, "You have to input all fields above to sign up", Toast.LENGTH_LONG).show()
        }
        else
        {
            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener{task ->
                    if (task.isSuccessful) {
                        firebaseUserID = mAuth.currentUser!!.uid
                        refUsers = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUserID)
//                        val userHashMap = HashMap<String, Any>()
//                        userHashMap["uid"] = firebaseUserID
//                        userHashMap["username"]
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