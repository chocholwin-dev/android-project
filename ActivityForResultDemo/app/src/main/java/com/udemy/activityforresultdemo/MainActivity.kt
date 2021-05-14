package com.udemy.activityforresultdemo

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    // companionはJavaのStaticと同じ
    companion object{
        private const val FIRST_ACTIVITY_REQUEST_CODE: Int = 1
        private const val SECOND_ACTIVITY_REQUEST_CODE: Int = 2

        const val NAME = "name"
        const val EMAIL = "email"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 「LUNCH FIRST ACTIVITY」ボタンを押す場合、
        btn_lunch_activity_first.setOnClickListener {
            val intent = Intent(this, FirstActivity::class.java)
            startActivityForResult(intent, FIRST_ACTIVITY_REQUEST_CODE)
        }

        // 「LUNCH SECOND ACTIVITY」ボタンを押す場合、
        btn_lunch_activity_second.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            startActivityForResult(intent, SECOND_ACTIVITY_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK){
            if(requestCode == FIRST_ACTIVITY_REQUEST_CODE){
                tv_first_activity_result.text = "First Activity Result Success!"
            }
            else if(requestCode == SECOND_ACTIVITY_REQUEST_CODE){
                // SecondActivityから戻ったIntent又はdataの値がNullではない場合、
                if(data != null){
                    // SecondActivityから戻ったNAMEを取得する
                    val name = data.getStringExtra(NAME)
                    val email = data.getStringExtra(EMAIL)
                    tv_second_activity_result.text = "$name => $email"
                }
            }
        }else if(resultCode == Activity.RESULT_CANCELED){
            Log.e("Canceled", "Cancelled")
            Toast.makeText(this@MainActivity,
                "Result Cancelled", Toast.LENGTH_SHORT).show()
        }
    }
}