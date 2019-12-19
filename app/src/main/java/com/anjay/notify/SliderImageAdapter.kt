package com.anjay.notify

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import com.smarteist.autoimageslider.SliderViewAdapter


class SliderImageAdapter(private val con: Context, var images: MutableList<String>) :
    SliderViewAdapter<SliderImageAdapter.SliderAdapterVH>() {


    override fun onCreateViewHolder(parent: ViewGroup): SliderAdapterVH {
        val inflate =
            LayoutInflater.from(parent.context).inflate(R.layout.ivs_item, null)
        return SliderAdapterVH(inflate)
    }

    override fun onBindViewHolder(viewHolder: SliderAdapterVH, position: Int) {

        viewHolder.imageViewBackground.setImage(ImageSource.asset("dummy.jpg"))

    }

    override fun getCount(): Int {
        //slider view count could be dynamic size
        return images.size
    }

    class SliderAdapterVH(var itemView: View) :
        SliderViewAdapter.ViewHolder(itemView) {
        var imageViewBackground: SubsamplingScaleImageView = itemView.findViewById(R.id.ivs_item)

    }
}