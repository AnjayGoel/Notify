package com.anjay.notify

import android.content.Context
import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView

class CardAdapter(var con: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var ma = con as MainActivity
    var dh = DataHandler.getInstance(con)


    override fun onViewRecycled(holderGeneral: RecyclerView.ViewHolder) {
        // Set all views to visible for next card
        if (holderGeneral !is CardHolder) {
            return
        }
        var holder = holderGeneral
        holder.ivContainer.visibility = View.VISIBLE
        holder.body.visibility = View.VISIBLE
        holder.h.visibility = View.VISIBLE
        holder.posttime.visibility = View.VISIBLE
        super.onViewRecycled(holder)
    }

    override fun onBindViewHolder(holderGeneral: RecyclerView.ViewHolder, position: Int) {
        lg("Binding $position")
        if (position == dh.cards.size) {
            return
        }

        var holder = holderGeneral as CardHolder
        holder.card = dh.cards[position]
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

    override fun getItemViewType(position: Int): Int {
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
        return if (position == dh.cards.size) 1 else 0

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 1) {
            var holder = EmptyHolder(
                LayoutInflater.from(con).inflate(
                    R.layout.loading_spinner,
                    parent,
                    false
                )
            )
            return holder
        }

        var holder = CardHolder(LayoutInflater.from(con).inflate(R.layout.card, parent, false))

        with(holder) {
            ivContainer.setOnClickListener {
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

            ivContainer.clipToOutline = true
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

        return holder
    }


    override fun getItemCount(): Int {
        return dh.cards.size + 1
    }

}

class CardHolder(root: View) : RecyclerView.ViewHolder(root) {
    var expanded = false
    var card = Card()
    var h: TextView = root.findViewById(R.id.h)
    var posttime: TextView = root.findViewById(R.id.posttime)
    var body: TextView = root.findViewById(R.id.body)
    var ivContainer: ViewGroup = root.findViewById(R.id.iv_container)
    var ivOne: ViewGroup = root.findViewById(R.id.image_one)
    var ivTwo: ViewGroup = root.findViewById(R.id.image_two)
    var ivThree: ViewGroup = root.findViewById(R.id.image_three_or_plus)
}

class EmptyHolder(root: View) : RecyclerView.ViewHolder(root)