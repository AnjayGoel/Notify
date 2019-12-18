package com.anjay.notify

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.anjay.notify.com.anjay.notify.SliderImageAdapter
import com.smarteist.autoimageslider.IndicatorAnimations
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView

class CardAdapter(var items: MutableList<Card>, val con: Context) :
    RecyclerView.Adapter<ViewHolder>() {


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var t = System.currentTimeMillis()
        var parent = holder.h.parent as ViewGroup

        holder.card = items[position]
        holder.expanded = false

        //heading
        if (holder.card.head == "") {
            parent.removeView(holder.h)                                                          //remove if no heading
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
            holder.body.text = holder.card.body
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
        lg("" + (System.currentTimeMillis() - t))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var holder =
            ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card, parent, false))

        holder.sv.sliderAdapter = SliderImageAdapter(con)
        holder.sv.setIndicatorAnimation(IndicatorAnimations.WORM) //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        holder.sv.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
        holder.sv.autoCycleDirection = SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH
        holder.sv.indicatorSelectedColor = Color.WHITE
        holder.sv.indicatorUnselectedColor = Color.GRAY
        holder.sv.scrollTimeInSec = 4 //set scroll delay in seconds :
        holder.sv.startAutoCycle()


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
    var body: TextView = root.findViewById(R.id.summary)
    var ivContainer: ViewGroup = root.findViewById(R.id.iv_container)
    var ivOne: ViewGroup = root.findViewById(R.id.image_one)
    var ivTwo: ViewGroup = root.findViewById(R.id.image_two)
    var ivThree: ViewGroup = root.findViewById(R.id.image_three_or_plus)
    var sv: SliderView = root.findViewById(R.id.imageSlider)

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
                if (card.images.size > 1) {

                    ivContainer.removeAllViews()
                    when (card.images.size) {
                        0 -> {
                        }
                        1 -> {
                            ivContainer.addView(ivOne)
                        }
                        2 -> {
                            ivContainer.addView(ivTwo)
                        }
                        3 -> {
                            ivContainer.addView(ivThree)
                        }
                        else -> {
                            ivContainer.addView(ivThree)
                        }

                    }

                }
            } else {
                expanded = true

                if (card.body.length > 350) {
                    body.text = HtmlCompat.fromHtml(
                        card.body + " <font color='#cccccc'> <u>View Less</u></font>",
                        HtmlCompat.FROM_HTML_MODE_LEGACY
                    )
                }
                if (card.images.size > 1) {
                    ivContainer.removeAllViews()
                    ivContainer.addView(sv)

                }
            }
        }
    }
}