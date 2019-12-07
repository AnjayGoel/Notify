package com.anjay.notify

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class CardAdapter(val items: MutableList<CardModel>, val context: Context) :
    RecyclerView.Adapter<ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var text = items[position].summary.replace("\\n", "\n")
        if (items[position].head == "") {
            (holder.h.parent as ViewGroup).removeView(holder.h)
        }
        holder.h.text = items[position].head
        if (text.length > 350) {
            text = text.substring(0, 350) + "..."
            holder.summary.text = HtmlCompat.fromHtml(
                "$text<font color='#cccccc'> <u>View More</u></font>",
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
            holder.summary.setOnClickListener { v ->
                var tb = v as TextView
                tb.text = items[position].summary
            }

        } else {
            holder.summary.text = items[position].summary
        }
        holder.time.text = Date(items[position].time).toString()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card,parent,false))
    }


    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return items.size
    }



}
class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    // Holds the TextView that will add each animal to
    var h: TextView = view.findViewById(R.id.h)
    var time: TextView = view.findViewById(R.id.posttime)
    var summary: TextView = view.findViewById(R.id.summary)
}