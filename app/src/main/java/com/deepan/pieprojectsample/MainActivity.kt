package com.deepan.pieprojectsample

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private var currentProgress: Int = 0
    private var currentSeconds = Calendar.getInstance().get(Calendar.SECOND)
    private var countDownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        try {
            if (currentSeconds>=30){
                currentSeconds -= 30
            }
            currentProgress = currentSeconds
            pieProgress.setProgress(currentProgress)
            //currentSeconds -= 30
            currentSeconds *= 1000
        } catch (e: Exception) {
            Log.e("CurrentSeconds", "---Current--${e.message}")
        }
        Log.d("onTick", "----$currentSeconds")
        countDownTimer = object : CountDownTimer(30000L, 1000) {
            override fun onTick(tick: Long) {
                if (currentProgress == 30) {
                    onFinish()
                } else {
                    currentProgress++
                    pieProgress.setProgress(currentProgress)
                    Log.e("CurrentSeconds", "---Current--${currentProgress}")
                }
            }

            override fun onFinish() {
                startCountDown()
            }
        }
        countDownTimer?.start()
    }

    /* private val countDownTimer = object : CountDownTimer(30000L, 1000) {
         override fun onTick(tick: Long) {
             if (currentProgress == 30) {
                 onFinish()
             } else {
                 currentProgress++
                 pieProgress.setProgress(currentProgress)
                 Log.e("CurrentSeconds", "---Current--${currentProgress}")
             }
         }

         override fun onFinish() {
             startCountDown()
         }
     }
 */



    private fun startCountDown() {
        this.countDownTimer?.cancel()
        countDownTimer = null
        currentProgress = 0
        countDownTimer = object : CountDownTimer(30000L, 1000) {
            override fun onTick(tick: Long) {
                if (currentProgress == 30) {
                    currentProgress = 0
                } else {
                    currentProgress++
                    pieProgress.setProgress(currentProgress)
                    Log.e("CurrentSeconds", "---Current--${currentProgress}")
                }
            }

            override fun onFinish() {
                startCountDown()
            }
        }
        countDownTimer?.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        finish()
        currentProgress = 0
        countDownTimer?.cancel()
        countDownTimer?.onFinish()
        countDownTimer = null
    }

}