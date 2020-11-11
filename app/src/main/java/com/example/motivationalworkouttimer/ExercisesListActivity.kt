package com.example.motivationalworkouttimer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import com.wajahatkarim3.easyvalidation.core.view_ktx.nonEmpty
import kotlinx.android.synthetic.main.activity_exercises_list.*
import kotlinx.android.synthetic.main.add_exercise.*
import kotlinx.android.synthetic.main.add_exercise.view.*
import kotlinx.android.synthetic.main.exercise_item.view.*
import kotlinx.android.synthetic.main.how_to_use.*
import kotlinx.android.synthetic.main.how_to_use.view.*

class ExercisesListActivity : AppCompatActivity() {

    val db = FirebaseFirestore.getInstance()
    private var adapter: ExerciseAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercises_list)

        excerciseRecyclerView.layoutManager = LinearLayoutManager(this)

        val query = db.collection("exercises").orderBy("title", Query.Direction.ASCENDING)

        val options = FirestoreRecyclerOptions.Builder<Exercise>().setQuery(query, Exercise::class.java).build()
        adapter = ExerciseAdapter(options)
        excerciseRecyclerView.adapter = adapter


        logoutFab.setOnClickListener{
            //sign user out
            FirebaseAuth.getInstance().signOut()
            finish()
            //reload Signin so providers reintialized
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }

        questionButton.setOnClickListener{
            val howToDialog = LayoutInflater.from(this).inflate(R.layout.how_to_use, null)
            val mBuilder = AlertDialog.Builder(this)
                .setView(howToDialog)
            val howToAlert = mBuilder.show()
            howToDialog.okButton.setOnClickListener{
                howToAlert.dismiss()
            }
        }


        addButton.setOnClickListener {
            val addExerciseDialog = LayoutInflater.from(this).inflate(R.layout.add_exercise, null)
            val mBuilder = AlertDialog.Builder(this)
                .setView(addExerciseDialog)
            val addExeciseAlert = mBuilder.show()
            addExerciseDialog.saveBtn.setOnClickListener{
                if ((addExerciseDialog.exerciseName.text.toString().nonEmpty()) && (addExerciseDialog.numOfCycles.text.toString().nonEmpty()) && (addExerciseDialog.prepareTime.text.toString().nonEmpty()) && (addExerciseDialog.workoutTime.text.toString().nonEmpty()) && (addExerciseDialog.restTime.text.toString().nonEmpty())  ) {
                    val exercise = Exercise()
                    exercise.title = addExerciseDialog.exerciseName.text.toString()
                    exercise.cycle = addExerciseDialog.numOfCycles.text.toString().toInt()
                    exercise.prepTime = addExerciseDialog.prepareTime.text.toString().toInt()
                    exercise.workTime = addExerciseDialog.workoutTime.text.toString().toInt()
                    exercise.restTime = addExerciseDialog.restTime.text.toString().toInt()

                    val db = FirebaseFirestore.getInstance().collection("exercises")
                    exercise.id = db.document().id
                    db.document(exercise.id!!).set(exercise)
                    Toast.makeText(this, "Exercise Added", Toast.LENGTH_LONG).show()
                    addExeciseAlert.dismiss()
                }
                else {
                    Toast.makeText(this, "Incomplete", Toast.LENGTH_LONG).show()
                }
            }

        }

    }



    override fun onStart() {
        super.onStart()
        adapter!!.startListening()

        val user = Firebase.auth.currentUser
        if (user == null) {
            val intent = Intent(applicationContext, SignInActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStop() {
        super.onStop()
        if (adapter != null) {
            adapter!!.stopListening()
        }
    }

    private inner class ExerciseViewHolder internal constructor(private val view: View) : RecyclerView.ViewHolder(view) {}

    private inner class ExerciseAdapter internal constructor(options: FirestoreRecyclerOptions<Exercise>) :
        FirestoreRecyclerAdapter<Exercise, ExerciseViewHolder>(options) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.exercise_item, parent, false)
            return  ExerciseViewHolder(view)
        }

        override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int, model: Exercise) {
            holder.itemView.exerciseTitle.text = model.title
            holder.itemView.exerciseCycle1.text = model.cycle.toString()
            holder.itemView.prepTime.text = model.prepTime.toString()
            holder.itemView.workTime.text = model.workTime.toString()
            holder.itemView.restTime1.text = model.restTime.toString()
        }
    }
}