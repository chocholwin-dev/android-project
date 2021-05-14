package com.udemy.sqlitedemo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.items_row.view.*

class ItemAdapter(val context: Context, val items: ArrayList<EmpModelClass>):
    RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        // Holds the textView which will add each item to
        val llMain = view.llMain
        val tvName = view.tvName
        val tvEmailId = view.tvEmailId
        val ivEdit = view.ivEdit
        val ivDelete = view.ivDelete
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.items_row,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items.get(position)

        holder.tvName.text = item.name
        holder.tvEmailId.text = item.email

        // Updating the background color according to odd/even positions in the array list
        if(position % 2 == 0){
            holder.llMain.setBackgroundColor(
                ContextCompat.getColor(context, R.color.colorPrimary)
            )
        } else {
            holder.llMain.setBackgroundColor(
                ContextCompat.getColor(context, R.color.colorAccent)
            )
        }

        // When click Edit icon
        holder.ivEdit.setOnClickListener { view ->
            if(context is MainActivity){
                context.updateRecordDialog(item)
            }
        }

        // When click Delete icon
        holder.ivDelete.setOnClickListener { view ->
            if(context is MainActivity){
                context.deleteRecordAlertDialog(item)
            }
        }
    }

}