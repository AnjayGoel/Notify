package com.anjay.notify

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray

class CardAdapter (val items :JSONArray, val context: Context) : RecyclerView.Adapter<ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var data = DataHandler.cardFromString(items[position].toString())
        holder.h.text = data.head
        holder.summary.text = data.summary
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card,parent,false))
    }


    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return items.length()
    }



}
class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    // Holds the TextView that will add each animal to
    var h: TextView = view.findViewById(R.id.h) as TextView
    var summary = view.findViewById(R.id.summary) as TextView
}