package com.anjay.notify

import android.content.Context
import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.card.view.*

class CardAdapter(var items: MutableList<Card>, con: Context) :
    RecyclerView.Adapter<ViewHolder>() {
    var ma = con as MainActivity


    override fun onViewRecycled(holder: ViewHolder) {                                               // Set all views to visible for next card
        holder.ivContainer.visibility = View.VISIBLE
        holder.body.visibility = View.VISIBLE
        holder.h.visibility = View.VISIBLE
        holder.posttime.visibility = View.VISIBLE
        super.onViewRecycled(holder)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.card = items[position]
        holder.expanded = false
        ma.iv.setImages(holder.card.images)
        //heading
        if (holder.card.head == "") {
            holder.h.visibility = View.GONE
        } else holder.h.text = holder.card.head

        //body
        if (holder.card.body.isEmpty()) {
            holder.body.visibility = View.GONE
        } else if (holder.card.body.length > 350) {
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
        Linkify.addLinks(holder.body, Linkify.ALL)
        //date
        holder.posttime.text = timeToString(holder.card.timestamp)

        //images
        if (holder.card.images.toString() == "[]") {
            holder.ivContainer.visibility = View.GONE
        } else {
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
        }
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

class ViewHolder(root: View) : RecyclerView.ViewHolder(root) {
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
        root.iv_container.clipToOutline = true
        body.setOnClickListener { v ->

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