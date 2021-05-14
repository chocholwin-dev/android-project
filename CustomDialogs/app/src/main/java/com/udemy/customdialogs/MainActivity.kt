package com.udemy.customdialogs

import android.app.Dialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_custom.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // SnackBarチュートリアル
        image_button.setOnClickListener {view ->
            Snackbar.make(view,
                "You have clicked image button", Snackbar.LENGTH_LONG).show()
        }

        // アラートダイアログチュートリアル
        btn_alert_dialog.setOnClickListener {
            // アラートダイアログを実行する
            alertDialogFunction()
        }

        // Customダイアログ
        btn_custom_dialog.setOnClickListener {
            // Customダイアログを表示する
            customDialogFunction()
        }

        // Custom Progress Dialog
        btn_custom_progress_dialog.setOnClickListener {
            // Custom Progressダイアログを表示する
            customProgressDialogFunction()
        }
    }

    /**
     * Method is used to show alert dialog
     */
    private fun alertDialogFunction(){
        // Use the Builder class for convenient dialog construction
        val builder = AlertDialog.Builder(this)

        // set title for alert dialog
        builder.setTitle("Alert")

        // set message for alert dialog
        builder.setMessage("This is Alert Dialog. Which is used to show alerts in our app.")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        // performing positive action
        builder.setPositiveButton("Yes"){ dialogInterface, which ->
            Toast.makeText(applicationContext,
                "Clicked Yes", Toast.LENGTH_SHORT).show()
            dialogInterface.dismiss()
        }

        // performing cancel action
        builder.setNeutralButton("Cancel"){dialogInterface, which ->
            Toast.makeText(applicationContext,
                "Clicked Cancel\n Operation Cancel", Toast.LENGTH_SHORT).show()
            dialogInterface.dismiss()
        }

        // performing negative action
        builder.setNegativeButton("No"){dialogInterface, which ->
            Toast.makeText(applicationContext,
                "Clicked No", Toast.LENGTH_SHORT).show()
            dialogInterface.dismiss()
        }

        // Create the alert dialog
        val alertDialog: AlertDialog = builder.create()

        // Set other dialog properties
        // Will not allow user to cancel after clicking on remaining screen area
        alertDialog.setCancelable(false)
        // Show the dialog to UI
        alertDialog.show()
    }

    /**
     * Method is used to show custom dialog
     */
    private fun customDialogFunction(){
        val customDialog = Dialog(this)
        /*
        Set the screen content from a layout resource.
        The resource will be inflated, adding all top-level views to the screen.
        */
        customDialog.setContentView(R.layout.dialog_custom)

        // Submitボタンを押す場合、
        customDialog.tv_submit.setOnClickListener (View.OnClickListener {
            Toast.makeText(applicationContext,
                "Clicked submit", Toast.LENGTH_LONG).show()
            customDialog.dismiss()
        })

        // Cancelボタンを押す場合、
        customDialog.tv_cancel.setOnClickListener (View.OnClickListener {
            Toast.makeText(applicationContext,
                "Clicked cancel", Toast.LENGTH_LONG).show()
            customDialog.dismiss()
        })

        // Start the dialog and display it on screen
        customDialog.show()
    }

    /**
     * Method is used to show custom progress dialog
     */
    private fun customProgressDialogFunction(){
        val customProgressDialog = Dialog(this)

        /*
        Set the screen content from a layout resource.
        The resource will be inflated, adding all top-level views to the screen.
        */
        customProgressDialog.setContentView(R.layout.dialog_custom_progress)

        // Start the dialog and display it on screen
        customProgressDialog.show()
    }
}