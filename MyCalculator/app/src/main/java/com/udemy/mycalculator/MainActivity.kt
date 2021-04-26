package com.udemy.mycalculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var lastNumeric: Boolean = false
    var lastDot: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    // 「0~9」ボタンを押す場合
    fun onDigit(view: View){
        // 「0~9」ボタンの値を取ってTextViewに付け加える
        tvInput.append((view as Button).text)
        lastNumeric = true
    }

    // 「CLR」ボタンを押す場合
    fun onClear(view: View){
        tvInput.text = ""
        lastDot = false
        lastNumeric = false
    }

    // 「.」ボタンを押す場合
    fun onDecimalPoint(view: View){
        // 「.」が複数回入力出来ないように制限する
        if(lastNumeric && !lastDot){
            tvInput.append(".")
            lastNumeric = false
            lastDot = true
        }
    }

    // 「=」ボタンを押す場合
    fun onEqual(view: View){
        // TextViewに桁がある場合、
        if(lastNumeric){

            // TextViewの値を一つの変数に設定する
            var tvValue = tvInput.text.toString()
            var prefix = ""

            try {
                // 桁の前にマイナス記号がある場合、
                if(tvValue.startsWith("-")){
                    prefix = "-"
                    tvValue = tvValue.substring(1)
                }

                // TextViewに引き算記号が含まれる場合、
                if(tvValue.contains("-")){

                    // TextViewの値をマイナス記号で分ける
                    val splitValue = tvValue.split("-")

                    var one = splitValue[0]
                    var two = splitValue[1]

                    // 桁の前にマイナス記号がある場合、
                    if(prefix.isNotEmpty()){
                        one = prefix + one
                    }

                    // 分けた二つの桁を引いてTextViewのtextに設定する
                    tvInput.text = removeZeroAfterDot((one.toDouble() - two.toDouble()).toString())
                }
                // TextViewに足し算記号が含まれる場合、
                else if(tvValue.contains("+")){

                    // TextViewの値をマイナス記号で分ける
                    val splitValue = tvValue.split("+")

                    var one = splitValue[0]
                    var two = splitValue[1]

                    // 桁の前にマイナス記号がある場合、
                    if(prefix.isNotEmpty()){
                        one = prefix + one
                    }

                    // 分けた二つの桁を引いてTextViewのtextに設定する
                    tvInput.text = removeZeroAfterDot((one.toDouble() + two.toDouble()).toString())
                }
                // TextViewに掛け算記号が含まれる場合、
                else if(tvValue.contains("*")){

                    // TextViewの値をマイナス記号で分ける
                    val splitValue = tvValue.split("*")

                    var one = splitValue[0]
                    var two = splitValue[1]

                    // 桁の前にマイナス記号がある場合、
                    if(prefix.isNotEmpty()){
                        one = prefix + one
                    }

                    // 分けた二つの桁を引いてTextViewのtextに設定する
                    tvInput.text = removeZeroAfterDot((one.toDouble() * two.toDouble()).toString())
                }
                // TextViewに割り算記号が含まれる場合、
                else if(tvValue.contains("/")){

                    // TextViewの値をマイナス記号で分ける
                    val splitValue = tvValue.split("/")

                    var one = splitValue[0]
                    var two = splitValue[1]

                    // 桁の前にマイナス記号がある場合、
                    if(prefix.isNotEmpty()){
                        one = prefix + one
                    }

                    // 二つ目の値が０になっている場合、
                    if(two == "0"){
                        Toast.makeText(this, "0で割ることはできません。", Toast.LENGTH_SHORT).show()
                    } else {
                        // 分けた二つの桁を引いてTextViewのtextに設定する
                        tvInput.text = removeZeroAfterDot((one.toDouble() / two.toDouble()).toString())
                    }
                }

            }catch (e: ArithmeticException){
                e.printStackTrace()
            }
        }
    }

    // 結果の後ろに「.0」が含まれる場合、「.0」を削除する
    private fun removeZeroAfterDot(result: String) : String{
        var value = result
        if(result.contains(".0")){
            value = result.substring(0, result.length - 2)
        }
        return value
    }

    // 「+、-、*、/」ボタンを押す場合
    fun onOperator(view: View){
        if(lastNumeric && !isOperatorAdded(tvInput.text.toString())){
            tvInput.append((view as Button).text)
            lastNumeric = false
            lastDot = false
        }
    }

    private fun isOperatorAdded(value: String): Boolean{
        return if(value.startsWith("-")){
            false
        }else{
            value.contains("/") || value.contains("*")
                    || value.contains("-") || value.contains("+")
        }
    }
}