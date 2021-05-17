package com.udemy.happyplaces.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.udemy.happyplaces.R
import com.udemy.happyplaces.adapters.HappyPlacesAdapter
import com.udemy.happyplaces.database.DatabaseHandler
import com.udemy.happyplaces.models.HappyPlaceModel
import com.udemy.happyplaces.utils.SwipeToEditCallback
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Setting an click event for Fab Button and calling the AddHappyPlaceActivity.
        fabAddHappyPlace.setOnClickListener {
            val intent = Intent(this@MainActivity, AddHappyPlaceActivity::class.java)
            startActivityForResult(intent, ADD_PLACE_ACTIVITY_REQUEST_CODE)
        }

        // Calling an function which have created for getting list when activity is launched.
        getHappyPlacesListFromLocalDB()
        // END
    }

    /**
     * Call Back method  to get the Message form other Activity
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // check if the request code is same as what is passed  here it is 'ADD_PLACE_ACTIVITY_REQUEST_CODE'
        if (requestCode == ADD_PLACE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                getHappyPlacesListFromLocalDB()
            } else {
                Log.e("Activity", "Cancelled or Back Pressed")
            }
        }
    }

    /**
     * A function to get the list of happy place from local database.
     */
    private fun getHappyPlacesListFromLocalDB() {

        val dbHandler = DatabaseHandler(this)

        val getHappyPlacesList = dbHandler.getHappyPlacesList()

        //  and passing the list to recyclerview to populate in UI.)
        if (getHappyPlacesList.size > 0) {
            rv_happy_places_list.visibility = View.VISIBLE
            tv_no_records_available.visibility = View.GONE

            setupHappyPlacesRecyclerView(getHappyPlacesList)
        } else {
            rv_happy_places_list.visibility = View.GONE
            tv_no_records_available.visibility = View.VISIBLE
        }
        // END
    }
    // END

    /**
     * A function to populate the recyclerview to the UI.
     */
    private fun setupHappyPlacesRecyclerView(happyPlacesList: ArrayList<HappyPlaceModel>) {

        rv_happy_places_list.layoutManager = LinearLayoutManager(this)
        rv_happy_places_list.setHasFixedSize(true)

        val placesAdapter = HappyPlacesAdapter(this, happyPlacesList)
        rv_happy_places_list.adapter = placesAdapter

        // Bind the onclickListener with adapter onClick function
        placesAdapter.setOnClickListener(object : HappyPlacesAdapter.OnClickListener {
            override fun onClick(position: Int, model: HappyPlaceModel) {

                val intent = Intent(this@MainActivity, HappyPlaceDetailActivity::class.java)

                // Pass the HappyPlaceDetails data model class to the detail activity.
                // Passing the complete serializable data class to the detail activity using intent.
                intent.putExtra(EXTRA_PLACE_DETAILS, model)

                startActivity(intent)
            }
        })
        // END

        // Bind the edit feature class to recyclerview
        // START
        val editSwipeHandler = object : SwipeToEditCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // Call the adapter function when it is swiped)
                val adapter = rv_happy_places_list.adapter as HappyPlacesAdapter
                adapter.notifyEditItem(
                    this@MainActivity,
                    viewHolder.adapterPosition,
                    ADD_PLACE_ACTIVITY_REQUEST_CODE
                )
                // END
            }
        }
        val editItemTouchHelper = ItemTouchHelper(editSwipeHandler)
        editItemTouchHelper.attachToRecyclerView(rv_happy_places_list)
        // END
    }
    // END

    companion object {
        private const val ADD_PLACE_ACTIVITY_REQUEST_CODE = 1

        // TODO (Step 2: Create a constant which will be used to put and get the data using intent from one activity to another.)
        // START
        internal const val EXTRA_PLACE_DETAILS = "extra_place_details"
        // END
    }
}