package com.anjay.notify

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView

class CardAdapter(val items: MutableList<CardModel>, val context: Context) :
    RecyclerView.Adapter<ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var card = items[position]
        var parent: ViewGroup = holder.h.parent as ViewGroup
        var text = card.summary.replace("\\n", "\n")

        //heading
        if (card.head == "") {
            (parent).removeView(holder.h)                                                          //remove if no heading
        } else holder.h.text = card.head

        //body
        if (text.length > 350) {
            text = text.substring(0, 350) + "..."
            holder.summary.text = HtmlCompat.fromHtml(
                "$text<font color='#cccccc'> <u>View More</u></font>",
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
            holder.summary.setOnClickListener { v ->
                //view more if text to long
                var tb = v as TextView
                tb.text = card.summary
            }

        } else {
            holder.summary.text = card.summary
        }

        //date
        holder.posttime.text = timeFromString(card.time)
        Log.d("sb", "" + card.images.size)

        //images
        when (card.images.size) {
            0 -> {
                parent.removeView(holder.iv_one)
                parent.removeView(holder.iv_two)
                parent.removeView(holder.iv_three_or_plus)
            }
            1 -> {
                parent.removeView(holder.iv_two)
                parent.removeView(holder.iv_three_or_plus)
            }
            2 -> {
                parent.removeView(holder.iv_one)
                parent.removeView(holder.iv_three_or_plus)
            }
            3 -> {
                parent.removeView(holder.iv_two)
                parent.removeView(holder.iv_one)
            }
            else -> {
                parent.removeView(holder.iv_two)
                parent.removeView(holder.iv_one)
            }


        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card, parent, false))
    }


    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return items.size
    }


}

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    // Holds the TextView that will add each animal to
    var h: TextView = view.findViewById(R.id.h)
    var posttime: TextView = view.findViewById(R.id.posttime)
    var summary: TextView = view.findViewById(R.id.summary)
    var iv_one: ViewGroup = view.findViewById(R.id.image_one)
    var iv_two: ViewGroup = view.findViewById(R.id.image_tow)
    var iv_three_or_plus: ViewGroup = view.findViewById(R.id.image_three_or_plus)
}