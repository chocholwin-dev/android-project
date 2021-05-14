package com.udemy.a7minuteworkout

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_exercise.*
import kotlinx.android.synthetic.main.dialog_custom_back_confirmation.*
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    // For Rest View
    private var restTimer: CountDownTimer? = null
    private var restProgress = 0
    private var restTimerDuration: Long = 1

    // For Exercise View
    private var exerciseTimer: CountDownTimer? = null
    private var exerciseProgress = 0
    private var exerciseTimerDuration: Long = 1

    private var exerciseList: ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1

    // Text To Speechを宣言する
    private var tts: TextToSpeech? = null

    // MediaPlayerを宣言する
    private var player: MediaPlayer? = null

    // ExerciseStatusAdapterを宣言する
    private var exerciseAdapter: ExerciseStatusAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)

        // ActionBarを設定する
        setSupportActionBar(toolbar_exercise_activity)
        val actionbar = supportActionBar

        // Text To Speechを初期化する
        tts = TextToSpeech(this, this)

        // actionbarがある場合、Home画面に戻るやじるしを設定する
        if(actionbar != null){
            actionbar.setDisplayHomeAsUpEnabled(true)
        }

        // ActionBarのやじるしをクリックするとHome画面に戻る
        toolbar_exercise_activity.setNavigationOnClickListener {
            // CustomDialogを表示する
            customDialogForBackButton()
        }

        // Exerciseを設定する
        exerciseList = Constants.defaultExerciseList()

        // Start Count Down
        setupRestView()

        // set up exercise status recycler view
        setupExerciseStatusRecyclerView()
    }

    /**
     * RestProgressを設定する関数
     */
    private fun setRestProgressBar(){
        progressBar.progress = restProgress

        restTimer = object : CountDownTimer(restTimerDuration * 1000, 1000){

            override fun onTick(millisUntilFinished: Long) {
                restProgress++
                // CurrentProgressを設定する
                progressBar.progress = restTimerDuration.toInt() - restProgress

                // CurrentTimerを設定する
                tvTimer.text = (restTimerDuration.toInt() - restProgress).toString()
            }

            override fun onFinish() {
                // ExerciseCurrentPositionを増加する
                currentExercisePosition++

                //
                exerciseList!![currentExercisePosition].setIsSelected(true)
                exerciseAdapter!!.notifyDataSetChanged()


                // RestViewが終わるとExerciseViewをStartする
                setupExerciseView()
            }
        }.start()
    }

    /**
     * ExerciseProgressを設定する関数
     */
    private fun setExerciseProgressBar(){
        progressBarExercise.progress = exerciseProgress

        exerciseTimer = object : CountDownTimer(exerciseTimerDuration * 1000, 1000){

            override fun onTick(millisUntilFinished: Long) {
                exerciseProgress++
                // CurrentProgressを設定する
                progressBarExercise.progress = exerciseTimerDuration.toInt() - exerciseProgress

                // CurrentTimerを設定する
                tvExerciseTimer.text = (exerciseTimerDuration.toInt() - exerciseProgress).toString()
            }

            override fun onFinish() {
                if(currentExercisePosition < exerciseList?.size!! - 1){
                    exerciseList!![currentExercisePosition].setIsSelected(false)
                    exerciseList!![currentExercisePosition].setIsCompleted(true)
                    exerciseAdapter!!.notifyDataSetChanged()
                    setupRestView()
                }else {
                    // ExerciseActivityを閉じる
                    finish()
                    // 全てのExerciseが終わる場合、FinishActivityに移動する
                    val intent = Intent(this@ExerciseActivity, FinishActivity::class.java)
                    startActivity(intent)
                }
            }
        }.start()
    }

    override fun onDestroy() {
        if(restTimer != null){
            restTimer!!.cancel()
            restProgress = 0
        }

        if(exerciseTimer != null){
            exerciseTimer!!.cancel()
            exerciseProgress = 0
        }

        if(tts != null){
            tts!!.stop()
            tts!!.shutdown()
        }

        if(player != null){
            player!!.stop()
        }
        super.onDestroy()
    }

    private fun setupRestView(){
        // MediaPlayerを作成する
        try {
            //val uri = Uri.parse("android:resource://com.udemy.a7minuteworkout" + R.raw.press_start)
            player = MediaPlayer.create(applicationContext, R.raw.press_start)
            player!!.isLooping = false
            player!!.start()
        }catch (e: Exception){
            e.printStackTrace()
        }

        llRestView.visibility = View.VISIBLE
        llExerciseView.visibility = View.GONE

        if(restTimer != null){
            // Default値を設定する
            restTimer!!.cancel()
            restProgress = 0
        }
        setRestProgressBar()

        // UpComingExercise名を設定する
        tvUpComingExerciseName.text = exerciseList!![currentExercisePosition + 1].getName()
    }

    private fun setupExerciseView(){
        // RestViewを非表示にしてExerciseViewを表示する
        llRestView.visibility = View.GONE
        llExerciseView.visibility = View.VISIBLE

        if(exerciseTimer != null){
            // Default値を設定する
            exerciseTimer!!.cancel()
            exerciseProgress = 0
        }

        // Speak the exercise name
        speakOut(exerciseList!![currentExercisePosition].getName())

        setExerciseProgressBar()

        // Exerciseデータを設定する
        ivImage.setImageResource(exerciseList!![currentExercisePosition].getImage())
        tvExerciseName.text = exerciseList!![currentExercisePosition].getName()

    }

    override fun onInit(status: Int) {
        if(status == TextToSpeech.SUCCESS){
            val result = tts!!.setLanguage(Locale.US)
            if(result == TextToSpeech.LANG_MISSING_DATA
                || result == TextToSpeech.LANG_NOT_SUPPORTED){
                Log.e("TTS", "The language specified is not supported.")
            }
        }else{
            Log.e("TTS", "Initialization Failed!")
        }
    }

    /**
     * Function is used to speak the text
     */
    private fun speakOut(text: String){
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    /**
     * Function is used to show exercise status recycler view
     */
    private fun setupExerciseStatusRecyclerView(){
        // Creates a horizontal Linear Layout Manager
        rvExerciseStatus.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.HORIZONTAL, false)

        // Access the RecyclerView Adapter and load the data into it
        exerciseAdapter = ExerciseStatusAdapter(exerciseList!!, this)
        rvExerciseStatus.adapter = exerciseAdapter
    }

    /**
     * Function is used to show custom dialog for back button
     */
    private fun customDialogForBackButton(){
        val customDialog = Dialog(this)
        customDialog.setContentView(R.layout.dialog_custom_back_confirmation)

        // Yesボタンを押す場合、
        customDialog.btnYes.setOnClickListener {
            // ExerciseActivityを閉じてMainActivityに移動する
            finish()
            // shutdown the custom dialog
            customDialog.dismiss()
        }

        // Noボタンを押す場合、
        customDialog.btnNo.setOnClickListener {
            // shutdown the custom dialog
            customDialog.dismiss()
        }

        // CustomDialogを表示する
        customDialog.show()
    }
}