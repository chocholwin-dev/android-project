package com.udemy.quizapp

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_quiz_questions.*

class QuizQuestionsActivity : AppCompatActivity(), View.OnClickListener {
    private var mCurrentPosition: Int = 1
    private var mQuestionsList: ArrayList<Question>? = null
    private var mSelectedOptionPosition: Int = 0
    private var mCorrectAnswers: Int = 0
    private var mUserName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_questions)

        // MainActivityから渡したユーザー名を取得する
        mUserName = intent.getStringExtra(Constants.USER_NAME)

        // QuestionsListを取得する
        mQuestionsList = Constants.getQuestions()
        Log.i("Question Size", "${mQuestionsList!!.size}")

        // Questionデータを設定する
        setQuestion()

        tv_option_one.setOnClickListener(this)
        tv_option_two.setOnClickListener(this)
        tv_option_three.setOnClickListener(this)
        tv_option_four.setOnClickListener(this)

        btn_submit.setOnClickListener(this)
    }

    // Questionデータを設定する
    private fun setQuestion(){
        // QuestionsListからQuestionを取得する
        //val mCurrentPosition = 1
        val question: Question? = mQuestionsList!![mCurrentPosition - 1]

        // DefaultTextViewを設定する
        defaultOptionsView()

        if(mCurrentPosition == mQuestionsList!!.size){
            btn_submit.text = "FINISH"
        }else {
            btn_submit.text = "SUBMIT"
        }

        // ProgressBarのprogressを設定する
        progressBar.progress = mCurrentPosition
        tv_progress.text = "$mCurrentPosition" + "/" + progressBar.max

        tv_question.text = question!!.question
        iv_image.setImageResource(question.image)
        tv_option_one.text = question.optionOne
        tv_option_two.text = question.optionTwo
        tv_option_three.text = question.optionThree
        tv_option_four.text = question.optionFour
    }

    // DefaultOptionTextViewを設定する
    private fun defaultOptionsView(){
        val options = ArrayList<TextView>()
        options.add(0, tv_option_one)
        options.add(1, tv_option_two)
        options.add(2, tv_option_three)
        options.add(3, tv_option_four)

        for(option in options){
            option.setTextColor(Color.parseColor("#7A8089"))
            option.typeface = Typeface.DEFAULT
            option.background = ContextCompat.getDrawable(
                this,
                R.drawable.default_option_border_bg
            )
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.tv_option_one -> {
                selectedOptionView(tv_option_one, 1)
            }
            R.id.tv_option_two -> {
                selectedOptionView(tv_option_two, 2)
            }
            R.id.tv_option_three -> {
                selectedOptionView(tv_option_three, 3)
            }
            R.id.tv_option_four -> {
                selectedOptionView(tv_option_four, 4)
            }

            // Submitボタンを押す場合、
            R.id.btn_submit -> {

                // SelectedOptionPositionが０になっている場合、
                if(mSelectedOptionPosition == 0){
                    // CurrentPositionを増加する
                    mCurrentPosition++

                    when{
                        // CurrentPositionがQuestionsListのサイズより小さいか等しい場合、
                        mCurrentPosition <= mQuestionsList!!.size -> {
                            setQuestion()
                        } else -> {
                            // ResultActivityに移動する
                            val intent = Intent(this, ResultActivity::class.java)
                            intent.putExtra(Constants.USER_NAME, mUserName)
                            intent.putExtra(Constants.CORRECT_ANSWERS, mCorrectAnswers)
                            intent.putExtra(Constants.TOTAL_QUESTIONS, mQuestionsList!!.size)
                            startActivity(intent)
                            finish()
                        }
                    }
                // CurrentPositionが０ではない場合、
                }else{
                    // QuestionsListからQuestionを取得する
                    val question = mQuestionsList?.get(mCurrentPosition -1)

                    // 答えが間違っている場合、選択したOptionのバックグラウンドを赤色に変更する
                    if(question!!.correctAnswer != mSelectedOptionPosition){
                        answerView(mSelectedOptionPosition, R.drawable.wrong_option_border_bg)
                    }else{
                        mCorrectAnswers++
                    }

                    // 正しい答えOptionのバックグラウンドを緑色に変更する
                    answerView(question.correctAnswer, R.drawable.correct_option_border_bg)

                    // 全ての質問を答えた場合、ボタン名を「FINISH」に変更する
                    if(mCurrentPosition == mQuestionsList!!.size){
                        btn_submit.text = "FINISH"

                    // 全ての質問を答えない場合、ボタン名を「GO TO NEXT QUESTION」に変更する
                    }else{
                        btn_submit.text = "GO TO NEXT QUESTION"
                    }
                    mSelectedOptionPosition = 0
                }
            }
        }
    }

    // 一つのOptionを選択した場合、
    private fun selectedOptionView(tv: TextView,
                                   selectedOptionNum: Int){
        // 前の選択をリセットする
        defaultOptionsView()

        mSelectedOptionPosition = selectedOptionNum

        tv.setTextColor(Color.parseColor("#363A43"))
        tv.setTypeface(tv.typeface, Typeface.BOLD)
        tv.background = ContextCompat.getDrawable(
            this,
            R.drawable.selected_option_border_bg
        )
    }

    private fun answerView(answer: Int, drawableView: Int){
        when(answer){
            1 -> {
                tv_option_one.background = ContextCompat.getDrawable(
                    this,
                    drawableView
                )
            }
            2 -> {
                tv_option_two.background = ContextCompat.getDrawable(
                    this,
                    drawableView
                )
            }
            3 -> {
                tv_option_three.background = ContextCompat.getDrawable(
                    this,
                    drawableView
                )
            }
            4 -> {
                tv_option_four.background = ContextCompat.getDrawable(
                    this,
                    drawableView
                )
            }
        }
    }
}