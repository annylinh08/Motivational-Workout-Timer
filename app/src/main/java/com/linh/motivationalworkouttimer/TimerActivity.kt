package com.linh.motivationalworkouttimer

import android.media.MediaActionSound
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.WindowManager
import android.widget.TextView
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.activity_timer.*
import java.lang.Character.FORMAT

class TimerActivity : AppCompatActivity() {

    //code reference: https://github.com/AlexandreLadriere/Interval-Timer/blob/master/app/src/main/java/alexandre/ladriere/intervaltimer/TimerActivity.kt
    private var initCycle: Int = 0
    private var initPrep: Int = 0
    private var initWorkTime: Int = 0
    private var initRestTime: Int = 0
    private var currentCycle: Int = 0
    private var currentState: Int = 0
    private var currentTime: Int = 0
    private var timer: CountDownTimer? = null
    private var isPaused: Boolean = false
    private var isMuted: Boolean = false
    private lateinit var getReadySound: MediaPlayer
    private lateinit var startSound: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        exerTitle.text = intent.getStringExtra("title")?.toString()
        getReadySound = MediaPlayer.create(this, R.raw.beep_get_ready)
        startSound = MediaPlayer.create(this, R.raw.beep_start)
        actionButtons()
        getValues()
        getReady()

    }

    private fun actionButtons() {

        soundFab.setOnClickListener{
            isMuted = if (isMuted) {
                soundFab.setImageResource(R.drawable.ic_volume_up_white_36dp)
                false
            } else {
                soundFab.setImageResource(R.drawable.ic_volume_off_white_36dp)
                true
            }
        }

        pauseFab.setOnClickListener{
            if (!isPaused) {
                cancelTimer()
                pauseFab.setImageResource(R.drawable.ic_play_arrow_white_36dp)
                isPaused = true
            } else

                if (currentState != -1) {
                    pauseFab.setImageResource(R.drawable.ic_pause_white_36dp)
                    currentTime = time.text.toString().toInt()
                    startTimer(currentTime)
                    isPaused = false
                }
        }

        stopFab.setOnClickListener{
            cancelTimer()
            this.finish()
        }

    }

    private fun getReady() {
        currentState = 0
        stepText.text = "Workout"
        currentStep.text = "Prepare"
        time.text = intent.getStringExtra("prepTime")?.toString()
        val quotes = arrayOf("Success starts with self-discipline.","Don’t wish for a good body, work for it.", "A one hour workout is 4% of your day. No excuses.","What seems impossible today will one day become your warm-up.", "Hustle for that muscle.")
        motivateTextView.text = quotes.random()
        startTimer(initPrep + 1)
    }

    private fun workout() {
        currentState = 1
        stepText.text = "Rest"
        currentStep.text = "Workout"
        time.text = intent.getStringExtra("workTime")?.toString()
        val quotes = arrayOf("Push yourself because no one else is going to do it for you.","No pain, no gain. Shut up and train.","Train insane or remain the same.","Suck it up. And one day you won’t have to suck it in.","Motivation is what gets you started. Habit is what keeps you going","The pain you feel today, will be the strength you feel tomorrow.", "Remember, Rome wasn’t built in a day. Work hard, good results will come.","Sweat is magic. Cover yourself in it daily to grant your wishes.", "Sore. The most satisfying pain.")
        motivateTextView.text = quotes.random()
        startTimer(initWorkTime + 1)
    }

    private fun rest() {
        currentState = 2
        stepText.text = "Workout"
        currentStep.text = "Rest"
        time.text = intent.getStringExtra("restTime")?.toString()
        val quotes = arrayOf("Your body can stand almost anything. It’s your mind that you have to convince.","The body achieves what the mind believes.","The hard part isn’t getting your body in shape. The hard part is getting your mind in shape.", "You don’t have to be extreme, just consistent.”","When you feel like quitting think about why you started.", "If you still look good at the end of your workout, you didn’t train hard enough.”")
        motivateTextView.text = quotes.random()
        startTimer(initRestTime + 1)
        currentCycle -= 1
    }


    private fun doneWorkout() {
        currentState = -1
        nextText.text = ""
        stepText.text = ""
        currentStep.text = "Finish"
        wellDone.text = "WELL DONE"
        val quotes = arrayOf("Congratulations! You Made It Through the Challenge!", "Congratulations on completing the SWEAT Challenge!","Congrats! You slayed It!")
        motivateTextView.text = quotes.random()
        time.text= ""
    }

    private fun getValues() {
        initCycle = intent.getStringExtra("cycle")!!.toInt()
        currentCycle = initCycle
        initPrep = intent.getStringExtra("prepTime")!!.toInt()
        initWorkTime = intent.getStringExtra("workTime")!!.toInt()
        initRestTime = intent.getStringExtra("restTime")!!.toInt()
    }

    private fun updateTimeUI(textViewTime: TextView) {
        var currentTime = textViewTime.text.toString()
        var timeCount = currentTime.toInt()
        if (timeCount in 1..4){
            timeCount -= 1
            if (timeCount == 0) {
                if (!isMuted){
                    startSound.start()
                }
                timeCount = 0
            } else {
                if (!isMuted) {
                    getReadySound.start()
                }
            }
        } else if (timeCount != 0) {
            timeCount -= 1
        }

        currentTime = timeCount.toString()
        textViewTime.text = currentTime
    }

    private fun startTimer(sec: Int) {
        timer = object : CountDownTimer((sec * 1000).toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                updateTimeUI(time)
            }
            override fun onFinish() {
                if (currentCycle != 0) {
                    if (currentState == 0 || currentState == 2) {
                        workout()
                    } else if (currentState == 1) {
                        rest()
                    } else {
                        finish()
                    }
                } else {
                    doneWorkout()
                }
            }
        }
        (timer as CountDownTimer).start()

    }

    private fun cancelTimer() {
        timer?.cancel()
    }

    //cancel timer when close app
    override fun onDestroy() {
        super.onDestroy()
        cancelTimer()
    }

}