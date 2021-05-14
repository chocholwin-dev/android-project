package com.udemy.a7minuteworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_b_m_i.*
import java.math.BigDecimal
import java.math.RoundingMode

class BMIActivity : AppCompatActivity() {
    val METRIC_UNITS_VIEW = "METRIC_UNIT_VIEW"
    val US_UNITS_VIEW = "US_UNIT_VIEW"

    var currentVisibleView: String = METRIC_UNITS_VIEW

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_b_m_i)

        setSupportActionBar(toolbar_bmi_activity)
        val actionBar = supportActionBar
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.title = "CALCULATE BMI"
        }

        toolbar_bmi_activity.setNavigationOnClickListener {
            onBackPressed()
        }

        // Calculateボタンを押す場合、
        btnCalculateUnits.setOnClickListener {
            // Metric Units Viewの場合、
            if(currentVisibleView == METRIC_UNITS_VIEW) {
                // 入力項目をチェックする
                if(validateMetricUnits()){
                    // Height value in meter(m)
                    val heightValue = etMetricUnitHeight.text.toString().toFloat() / 100
                    // Weight value
                    val weightValue = etMetricUnitWeight.text.toString().toFloat()
                    // BMI value kg/(m*m)
                    val bmi = weightValue / (heightValue * heightValue)
                    // BMI結果を表示する
                    displayBMIResult(bmi)
                } else {
                    Toast.makeText(this,
                        "Please enter valid values.", Toast.LENGTH_SHORT).show()
                }
            }
            // US Units Viewの場合、
            else {
                if(validateUsUnits()){
                    // Height value in Feet(ft)
                    val usUnitHeightValueFeet: String = etUsUnitHeightFeet.text.toString()
                    // Height value in Inch(in)
                    val usUnitHeightValueInch: String = etUsUnitHeightInch.text.toString()
                    // Weight value
                    val usUnitWeightValue: Float = etUsUnitWeight.text.toString().toFloat()
                    // Height Value in Feet(ft)
                    val heightValue = usUnitHeightValueInch.toFloat() +
                            usUnitHeightValueFeet.toFloat() * 12
                    // BMI value 703*(lbs/(m*m))
                    val bmi = 703 * (usUnitWeightValue / (heightValue * heightValue))
                    // BMI結果を表示する
                    displayBMIResult(bmi)
                } else {
                    Toast.makeText(this,
                        "Please enter valid values.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // RadioButtonをクリックする場合、Viewの表示・非表示をチェックする
        makeVisibleMetricUnitsView()
        rgUnits.setOnCheckedChangeListener { group, checkedId ->
            if(checkedId == R.id.rbMetricUnits) {
                makeVisibleMetricUnitsView()
            } else {
                makeVisibleUsUnitsView()
            }
        }
    }

    /**
     * Metric Units Viewを表示する
     */
    private fun makeVisibleMetricUnitsView(){
        // Metric Units Viewを表示にする
        currentVisibleView = METRIC_UNITS_VIEW
        tilMetricUnitHeight.visibility = View.VISIBLE
        tilMetricUnitWeight.visibility = View.VISIBLE

        // 初期化する
        etMetricUnitWeight.text!!.clear()
        etMetricUnitHeight.text!!.clear()

        // Us Units Viewを非表示にする
        tilUsUnitWeight.visibility = View.GONE
        llUsUnitsHeight.visibility = View.GONE

        llDisplayBMIResult.visibility = View.GONE
    }

    /**
     * US Units Viewを表示する
     */
    private fun makeVisibleUsUnitsView(){
        // Metric Units Viewを表示にする
        currentVisibleView = US_UNITS_VIEW
        tilMetricUnitHeight.visibility = View.GONE
        tilMetricUnitWeight.visibility = View.GONE

        // 初期化する
        etUsUnitHeightFeet.text!!.clear()
        etUsUnitHeightInch.text!!.clear()
        etUsUnitWeight.text!!.clear()

        // Us Units Viewを非表示にする
        tilUsUnitWeight.visibility = View.VISIBLE
        llUsUnitsHeight.visibility = View.VISIBLE

        llDisplayBMIResult.visibility = View.GONE
    }

    /**
     * BMI値を比較して画面に表示する
     */
    private fun displayBMIResult(bmi: Float){
        val bmiLabel: String
        val bmiDescription: String
        if(bmi.compareTo(15f) <= 0){
            bmiLabel = "Very severely underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        }
        else if(bmi.compareTo(15f) > 0 && bmi.compareTo(16f) <= 0){
            bmiLabel = "Severely underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        }
        else if(bmi.compareTo(16f) > 0 && bmi.compareTo(18.5f) <= 0){
            bmiLabel = "Underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        }
        else if(bmi.compareTo(18.5f) > 0 && bmi.compareTo(25f) <= 0){
            bmiLabel = "Normal"
            bmiDescription = "Congratulations! You are in a good shape!"
        }
        else if(bmi.compareTo(25f) > 0 && bmi.compareTo(30f) <= 0){
            bmiLabel = "Overweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Workout more!"
        }
        else if(bmi.compareTo(30f) > 0 && bmi.compareTo(35f) <= 0){
            bmiLabel = "Obese Class | (Moderately obese)"
            bmiDescription = "Oops! You really need to take better care of yourself! Workout more!"
        }
        else if(bmi.compareTo(35f) > 0 && bmi.compareTo(40f) <= 0){
            bmiLabel = "Obese Class || (Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        }
        else {
            bmiLabel = "Obese Class ||| (Very severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        }

        // Viewに表示・非表示を設定する
        llDisplayBMIResult.visibility = View.VISIBLE

        /*tvYourBMI.visibility = View.VISIBLE
        tvBMIType.visibility = View.VISIBLE
        tvBMIValue.visibility = View.VISIBLE
        tvBMIDescription.visibility = View.VISIBLE*/

        // Round BMI value
        val bmiValue = BigDecimal(bmi.toDouble())
            .setScale(2, RoundingMode.HALF_EVEN).toString()

        // Viewに設定する
        tvBMIValue.text = bmiValue
        tvBMIType.text = bmiLabel
        tvBMIDescription.text = bmiDescription
    }

    /**
     * 入力項目をチェックする
     */
    private fun validateMetricUnits(): Boolean {
        var isValid = true

        if(etMetricUnitWeight.text.toString().isEmpty())
            isValid = false
        else if(etMetricUnitHeight.text.toString().isEmpty())
            isValid = false

        return isValid
    }

    /**
     * 入力項目をチェックする
     */
    private fun validateUsUnits(): Boolean {
        var isValid = true

        when {
            etUsUnitHeightFeet.text.toString().isEmpty() -> isValid = false
            etUsUnitHeightInch.text.toString().isEmpty() -> isValid = false
            etUsUnitWeight.text.toString().isEmpty() -> isValid = false
        }

        return isValid
    }
}