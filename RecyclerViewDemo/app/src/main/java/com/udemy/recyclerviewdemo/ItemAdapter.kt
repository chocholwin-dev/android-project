package com.udemy.recyclerviewdemo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.items_row.view.*

class ItemAdapter(val context: Context, val items: ArrayList<String>):
    RecyclerView.Adapter<ItemAdapter.ViewHolder>(){
    /**
     * Inflates the item view which is designed in xml layout file
     *
     * Create a new
     * {@link ViewHolder} and initializes some private fields to be used
     * by RecyclerView
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.items_row,
                parent,
                false
            )
        )
    }

    /**
     * Gets the number of items in the list
     */
    override fun getItemCount(): Int {
        return items.size
    }

    /**
     * Binds each item in the ArrayList to a view
     *
     * Call when a RecyclerView needs a new {@Link ViewHolder} of the given type to represent
     * an item.
     *
     * This new ViewHolder should be constructed with a New View that can represent the items
     * of the given type. You can either create a new View manually or inflate an xml
     * Layout file
     */
    override fun onBindViewHolder(holder: ItemAdapter.ViewHolder, position: Int) {
        val item = items[position]
        holder.tvItem.text = item

        // Updating the background color according to the odd/even positions
        if(position % 2 == 0){
            holder.tvItem.setBackgroundColor(
                ContextCompat.getColor(context, R.color.colorLightGray)
            )
        } else {
            holder.tvItem.setBackgroundColor(
                ContextCompat.getColor(context, R.color.colorWhite)
            )
        }
    }

    /**
     * A ViewHolder describes an item view and metadata about its place within
     */
    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        // Holds the TextView that will add each item to
        val tvItem = view.tvItem
    }
}