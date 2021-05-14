package com.udemy.a7minuteworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_finish.*
import java.text.SimpleDateFormat
import java.util.*

class FinishActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finish)

        setSupportActionBar(toolbar_finish_activity)
        val actionBar = supportActionBar
        // Back Buttonを設定する
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
        }

        // BackButtonを押す場合、
        toolbar_finish_activity.setNavigationOnClickListener {
            onBackPressed()
        }

        // Finishボタンを押す場合、
        btnFinish.setOnClickListener {
            finish()
        }

        // add date to database
        addDateToDatabase()
    }

    /**
     * Function to Add Date to Database
     */
    private fun addDateToDatabase(){
        // create calendar object
        val calendar = Calendar.getInstance()
        val datetime = calendar.time
        Log.i("DATE", " "+datetime)

        // format date
        val sdf = SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault())
        val date = sdf.format(datetime)

        val dbHandler = SqliteOpenHelper(this, null)
        dbHandler.addDate(date)
        Log.i("DATE", "Added")
    }
}