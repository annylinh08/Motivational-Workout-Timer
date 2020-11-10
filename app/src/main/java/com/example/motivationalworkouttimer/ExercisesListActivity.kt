package com.example.motivationalworkouttimer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_exercises_list.*
import kotlinx.android.synthetic.main.how_to_use.*
import kotlinx.android.synthetic.main.how_to_use.view.*

class ExercisesListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercises_list)

        logoutFab.setOnClickListener{
            //sign user out
            FirebaseAuth.getInstance().signOut()
            finish()
            //reload Signin so providers reintialized
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }

        questionButton.setOnClickListener{
            val howToDialog = LayoutInflater.from(this).inflate(R.layout.how_to_use, null);
            val mBuilder = AlertDialog.Builder(this)
                .setView(howToDialog)
            val howToAlert = mBuilder.show()
            howToDialog.okButton.setOnClickListener{
                howToAlert.dismiss()
            }
        }


        addButton.setOnClickListener {
            Toast.makeText(this@ExercisesListActivity, "Button Clicked ..", Toast.LENGTH_SHORT).show()
        }

    }
}