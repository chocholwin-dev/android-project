package com.udemy.sqlitedemo

import android.app.AlertDialog
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_update.*
import kotlinx.android.synthetic.main.items_row.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnAdd.setOnClickListener { view ->
            addRecord(view)
        }

        // show item list
        setupListOfDataIntoRecyclerView()
    }

    /**
     * Method for saving record in database
     */
    fun addRecord(view: View){
        val name = etName.text.toString()
        val email = etEmailId.text.toString()
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)

        if(name.isNotEmpty() && email.isNotEmpty()){
            val status = databaseHandler.addEmployee(EmpModelClass(0, name, email))
            if(status > -1){
                Toast.makeText(applicationContext,
                    "Record Saved", Toast.LENGTH_SHORT).show()

                // After saving record, clear name and email
                etName.text.clear()
                etEmailId.text.clear()

                setupListOfDataIntoRecyclerView()
            }
        } else {
            Toast.makeText(applicationContext,
                "Email and Name cannot be blank.", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Function is used to show the list of inserted data
     */
    private fun setupListOfDataIntoRecyclerView(){
        if(getItemsList().size > 0){
            rvItemsList.visibility = View.VISIBLE
            tvNoRecordsAvailable.visibility = View.GONE

            // set the LayoutManager that this RecyclerView will use
            rvItemsList.layoutManager = LinearLayoutManager(this)
            // Adapter class is initialized and passed it in the param
            val itemAdapter = ItemAdapter(this, getItemsList())
            // Adapter instance is set to the recyclerView to inflate the item layout
            rvItemsList.adapter = itemAdapter
        } else {
            rvItemsList.visibility = View.GONE
            tvNoRecordsAvailable.visibility = View.VISIBLE
        }
    }

    /**
     * Function is used to get the items list which is added in the database
     */
    private fun getItemsList(): ArrayList<EmpModelClass> {
        // creating the instance of DatabaseHandler class
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        // calling the viewEmployee method of DatabaseHandler class to read the items list
        return databaseHandler.viewEmployee()
    }

    /**
     * Function is used to update the items list which is added in the database
     */
    fun updateRecordDialog(empModelClass: EmpModelClass){
        val updateDialog = Dialog(this, R.style.Theme_Dialog)
        updateDialog.setCancelable(false)

        setContentView(R.layout.dialog_update)
        updateDialog.etName.setText(empModelClass.name)
        updateDialog.etEmailId.setText(empModelClass.email)

        updateDialog.tvUpdate.setOnClickListener(View.OnClickListener {
            val name = updateDialog.tvName.text.toString()
            val email = updateDialog.etEmailId.text.toString()
            val databaseHandler: DatabaseHandler = DatabaseHandler(this)

            if(name.isNotEmpty() && email.isNotEmpty()){
                // Update record
                val status = databaseHandler.updateEmployee(
                    EmpModelClass(empModelClass.id, empModelClass.name, empModelClass.email))

                if(status > -1){
                    Toast.makeText(applicationContext,
                        "Record Updated", Toast.LENGTH_LONG).show()

                    setupListOfDataIntoRecyclerView()

                    // Dialog will be dismissed
                    updateDialog.dismiss()
                }
            } else {
                Toast.makeText(applicationContext,
                    "Email and Name cannot be blank.", Toast.LENGTH_SHORT).show()
            }

        })

        updateDialog.tvCancel.setOnClickListener(View.OnClickListener {
            updateDialog.dismiss()
        })
    }

    /**
     * Function is used to delete the items list which is added in the database
     */
    fun deleteRecordAlertDialog(empModelClass: EmpModelClass){
        val builder = AlertDialog.Builder(this)
        // set title for alert dialog
        builder.setTitle("Delete Record")
        // set message for alert dialog
        builder.setMessage("Are you sure you want to delete ${empModelClass.id}")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        // performing positive action
        builder.setPositiveButton("Yes") {dialogInterface, which ->
            // creating the instance of DatabaseHandler class
            val databaseHandler: DatabaseHandler = DatabaseHandler(this)
            // calling the deleteEmployee method of databaseHandler class to delete record
            val status = databaseHandler.deleteEmployee(EmpModelClass(empModelClass.id, "", ""))
            if(status > -1){
                Toast.makeText(this,
                    "Record Delete Successfully", Toast.LENGTH_LONG).show()

                setupListOfDataIntoRecyclerView()
            }
            // dialog will be dismissed
            dialogInterface.dismiss()
        }

        // performing negative action
        builder.setPositiveButton("Yes") { dialogInterface, which ->
            dialogInterface.dismiss()
        }

        // Create the alert dialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        // Will not allow the user to cancel after
        alertDialog.setCancelable(false)
        // Show the dialog to UI
        alertDialog.show()
    }
}