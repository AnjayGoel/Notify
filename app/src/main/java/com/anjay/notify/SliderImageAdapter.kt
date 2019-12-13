package com.anjay.notify.com.anjay.notify

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.anjay.notify.R
import com.smarteist.autoimageslider.SliderViewAdapter
import java.io.File


class SliderImageAdapter(private val context: Context) :
    SliderViewAdapter<SliderImageAdapter.SliderAdapterVH>() {

    override fun onCreateViewHolder(parent: ViewGroup): SliderAdapterVH {
        val inflate =
            LayoutInflater.from(parent.context).inflate(R.layout.ivs_item, null)
        return SliderAdapterVH(inflate)
    }

    override fun onBindViewHolder(viewHolder: SliderAdapterVH, position: Int) {

        viewHolder.imageViewBackground.setImageBitmap(
            BitmapFactory.decodeFile(
                File(
                    context.getExternalFilesDir(
                        null
                    ), "dummy.jpg"
                ).absolutePath
            )
        )

    }

    override fun getCount(): Int {
        //slider view count could be dynamic size
        return 4
    }

    class SliderAdapterVH(var itemView: View) :
        SliderViewAdapter.ViewHolder(itemView) {
        var imageViewBackground: ImageView = itemView.findViewById(R.id.ivs_item)

    }
}