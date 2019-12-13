package com.anjay.notify

import android.content.Context
import android.graphics.Color
import android.util.Log
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

class CardAdapter(val items: List<Card>, val context: Context) :
    RecyclerView.Adapter<ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var card = items[position]
        var parent: ViewGroup? = holder.h.parent as? ViewGroup
        var text = card.body.replace("\\n", "\n")
        //heading
        if (card.head == "") {
            (parent)?.removeView(holder.h)                                                          //remove if no heading
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
                tb.text = card.body

                holder.iv_container.removeView(holder.iv_one)
                holder.iv_container.removeView(holder.iv_two)
                holder.iv_container.removeView(holder.iv_three_or_plus)
                holder.iv_container.addView(holder.sv)


            }

        } else {
            holder.summary.text = card.body
        }

        holder.sv.sliderAdapter = SliderImageAdapter(context)
        holder.sv.setIndicatorAnimation(IndicatorAnimations.WORM) //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        holder.sv.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
        holder.sv.autoCycleDirection = SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH
        holder.sv.indicatorSelectedColor = Color.WHITE
        holder.sv.indicatorUnselectedColor = Color.GRAY
        holder.sv.scrollTimeInSec = 4 //set scroll delay in seconds :
        holder.sv.startAutoCycle()

        //date
        holder.posttime.text = timeFromString(card.timestamp)
        Log.d("sb", "" + card.images!!.size)


        //images
        holder.iv_container.removeView(holder.sv)
        when (card.images!!.size) {
            0 -> {
                holder.iv_container.removeView(holder.iv_one)
                holder.iv_container.removeView(holder.iv_two)
                holder.iv_container.removeView(holder.iv_three_or_plus)
            }
            1 -> {
                holder.iv_container.removeView(holder.iv_two)
                holder.iv_container.removeView(holder.iv_three_or_plus)
            }
            2 -> {
                holder.iv_container.removeView(holder.iv_one)
                holder.iv_container.removeView(holder.iv_three_or_plus)
            }
            3 -> {
                holder.iv_container.removeView(holder.iv_two)
                holder.iv_container.removeView(holder.iv_one)
            }
            else -> {
                holder.iv_container.removeView(holder.iv_two)
                holder.iv_container.removeView(holder.iv_one)
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
    var iv_container: ViewGroup = view.findViewById(R.id.iv_container)
    var iv_one: ViewGroup = view.findViewById(R.id.image_one)
    var iv_two: ViewGroup = view.findViewById(R.id.image_two)
    var iv_three_or_plus: ViewGroup = view.findViewById(R.id.image_three_or_plus)
    var sv: SliderView = view.findViewById(R.id.imageSlider)
}