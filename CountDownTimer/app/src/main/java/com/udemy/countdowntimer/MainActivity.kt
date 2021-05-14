package com.udemy.countdowntimer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    // Variable for Timer which will be initialized later
    private var countDownTimer: CountDownTimer? = null
    // The duration of the Timer in milliseconds
    private var timerDuration: Long = 60000
    // pauseOffset = timerDuration - time left
    private var pauseOffset: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // tvTimerにTimeを設定する
        tvTimer.text = "${(timerDuration / 1000).toString()}"

        // Startボタンを押す場合、
        btnStart.setOnClickListener {
            startTimer(pauseOffset)
        }

        // Pauseボタンを押す場合、
        btnPause.setOnClickListener {
            pauseTimer()
        }

        // Resetボタンを押す場合、
        btnReset.setOnClickListener {
            resetTimer()
        }
    }

    /**
     * Function is used to start the timer of 60 seconds
     */
    private fun startTimer(pauseOffsetL: Long){
        countDownTimer = object : CountDownTimer(timerDuration - pauseOffsetL,
            1000){
            // Every 1000 milliseconds, onTick will be called
            override fun onTick(millisUntilFinished: Long) {
                pauseOffset = timerDuration - millisUntilFinished
                tvTimer.text = (millisUntilFinished / 1000).toString()
            }

            override fun onFinish() {
                Toast.makeText(this@MainActivity,
                    "Timer is finished", Toast.LENGTH_SHORT).show()
            }
        }.start()
    }

    /**
     * Function is used to pause the count down timer which is running
     */
    private fun pauseTimer(){
        if(countDownTimer != null){
            countDownTimer!!.cancel()
        }
    }

    /**
     * Function is used to reset the count down timer which is running
     */
    private fun resetTimer(){
        if(countDownTimer != null){
            countDownTimer!!.cancel()
            tvTimer.text = "${(timerDuration / 1000).toString()}"
            countDownTimer = null
            pauseOffset = 0
        }
    }
}