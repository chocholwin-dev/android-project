package com.udemy.recyclerviewdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {
    // Value for list view
    val LIST_VIEW = "LIST_VIEW"
    // Value for grid view
    val GRID_VIEW = "GRID_VIEW"

    // Variable is used to check which is the current visible view.
    // As Default it is list view
    var currentVisibleView: String = LIST_VIEW

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView()

        fabSwitch.setOnClickListener{view ->
            if(currentVisibleView == LIST_VIEW){
                
            }
        }
    }

    /**
     * Function is used to show the items in a list format
     */
    private fun listView(){
        // Current visible view is updated
        currentVisibleView = LIST_VIEW

        // Set the LayoutManager that this RecyclerView will use
        rvItemsList.layoutManager = LinearLayoutManager(this)

        // Adapter class is initialized and list is passed in the param
        val itemAdapter = ItemAdapter(this, getItemsList())

        // Adapter instance is set to the recyclerview to inflate the items.
        rvItemsList.adapter = itemAdapter
    }

    /**
     * Function is used to show the items in a grid format
     */
    private fun gridView(){
        // Current visible view is updated
        currentVisibleView = GRID_VIEW

        // Set the LayoutManager that this RecyclerView will use
        rvItemsList.layoutManager = GridLayoutManager(this, 2)

        // Adapter class is initialized and list is passed in the param
        val itemAdapter = ItemAdapter(this, getItemsList())

        // Adapter instance is set to the recyclerview to inflate the items.
        rvItemsList.adapter = itemAdapter
    }

    /**
     * Function is used to get the items list which is added to the list
     */
    private fun getItemsList(): ArrayList<String> {
        val list = ArrayList<String>()

        list.add("Item One")
        list.add("Item Two")
        list.add("Item Three")
        list.add("Item Four")
        list.add("Item Five")
        list.add("Item Six")
        list.add("Item Seven")
        list.add("Item Eight")
        list.add("Item Nine")
        list.add("Item Ten")
        list.add("Item Eleven")
        list.add("Item Twelve")
        return list
    }
}