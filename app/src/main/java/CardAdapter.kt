package com.anjay.notify

import android.content.Context
import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView

class CardAdapter(var items: MutableList<Card>, val con: Context) :
    RecyclerView.Adapter<ViewHolder>() {
    var ma = con as MainActivity

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var t = System.currentTimeMillis()
        var parent = holder.h.parent as ViewGroup?

        holder.card = items[position]
        holder.expanded = false
        ma.iv.setImages(holder.card.images)
        //heading
        if (holder.card.head == "") {
            parent?.removeView(holder.h)                                                          //remove if no heading
        } else holder.h.text = holder.card.head

        //body
        if (holder.card.body.length > 350) {
            holder.body.text = HtmlCompat.fromHtml(
                holder.card.body.substring(
                    0,
                    350
                ) + "...<font color='#cccccc'> <u>View More</u></font>",
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
        } else {
            holder.body.text =
                HtmlCompat.fromHtml(holder.card.body, HtmlCompat.FROM_HTML_MODE_LEGACY)
        }


        //date
        holder.posttime.text = timeFromString(holder.card.timestamp)

        //images
        holder.ivContainer.removeAllViews()
        when (holder.card.images.size) {
            0 -> {
            }
            1 -> {
                holder.ivContainer.addView(holder.ivOne)
            }
            2 -> {
                holder.ivContainer.addView(holder.ivTwo)
            }
            3 -> {
                holder.ivContainer.addView(holder.ivThree)
            }
            else -> {
                holder.ivContainer.addView(holder.ivThree)
            }

        }
        if (holder.card.images.toString() == "[]") {
            holder.ivContainer.removeAllViews() //Temp Hack
        }
        Linkify.addLinks(holder.body, Linkify.ALL)

        lg("" + (System.currentTimeMillis() - t))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var holder =
            ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card, parent, false))



        holder.ivContainer.setOnClickListener {
            ma.supportActionBar?.hide()
            ma.imageViewOnScreen = true
            ma.iv.setImages(holder.card.images)
            ma.addContentView(
                ma.iv,
                ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            )
        }


        return holder
    }


    override fun getItemCount(): Int {
        return items.size
    }


}

class ViewHolder(var root: View) : RecyclerView.ViewHolder(root) {
    var expanded = false
    var card = Card()
    var h: TextView = root.findViewById(R.id.h)
    var posttime: TextView = root.findViewById(R.id.posttime)
    var body: TextView = root.findViewById(R.id.body)
    var ivContainer: ViewGroup = root.findViewById(R.id.iv_container)
    var ivOne: ViewGroup = root.findViewById(R.id.image_one)
    var ivTwo: ViewGroup = root.findViewById(R.id.image_two)
    var ivThree: ViewGroup = root.findViewById(R.id.image_three_or_plus)

    init {

        body.setOnClickListener { v ->
            //view more if text to long
            lg("Clicked ${card.body.length} ${card.images.size} ${card.head}")

            if (card.body.length < 350 && card.images.size <= 1) return@setOnClickListener
            if (expanded) {
                expanded = false
                if (card.body.length > 350) {
                    body.text = HtmlCompat.fromHtml(
                        card.body.substring(
                            0,
                            350
                        ) + "...<font color='#cccccc'> <u>View More</u></font>",
                        HtmlCompat.FROM_HTML_MODE_LEGACY
                    )
                }
            } else {
                expanded = true

                if (card.body.length > 350) {
                    body.text = HtmlCompat.fromHtml(
                        card.body + " <font color='#cccccc'> <u>View Less</u></font>",
                        HtmlCompat.FROM_HTML_MODE_LEGACY
                    )
                }
            }
            Linkify.addLinks(body, Linkify.ALL)
        }
    }
}