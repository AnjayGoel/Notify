package com.anjay.notify.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.anjay.notify.R
import com.anjay.notify.lg
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomViewTarget
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import com.smarteist.autoimageslider.SliderViewAdapter
import kotlinx.android.synthetic.main.siv_item.view.*
import java.io.File


class SubsamplingScaleImageViewTarget(view: SubsamplingScaleImageView) :
    CustomViewTarget<SubsamplingScaleImageView, File>(view) {
    override fun onResourceReady(
        resource: File,
        transition: com.bumptech.glide.request.transition.Transition<in File>?
    ) {
        view.setImage(ImageSource.uri(Uri.fromFile(resource)))
    }

    override fun onLoadFailed(errorDrawable: Drawable?) {
        // Ignore
    }

    override fun onResourceCleared(placeholder: Drawable?) {
        // Ignore
    }
}

class SliderImageAdapter(private val con: Context, var images: MutableList<String>) :
    SliderViewAdapter<SliderImageAdapter.SliderAdapterVH>() {

    var iv = ImageView(con)
    val loader = CircularProgressDrawable(con)

    init {
        loader.strokeWidth = 5f
        loader.centerRadius = 30f
        loader.start()
    }

    override fun onCreateViewHolder(parent: ViewGroup): SliderAdapterVH {
        val inflate = LayoutInflater.from(parent.context).inflate(R.layout.siv_item, null)
        return SliderAdapterVH(
            inflate
        )
    }

    override fun onBindViewHolder(viewHolder: SliderAdapterVH, position: Int) {
        lg(images.toString())
        Glide.with(con).download(images[position])
            .into(
                SubsamplingScaleImageViewTarget(
                    viewHolder.imageViewBackground
                )
            )
    }

    override fun getCount(): Int {
        return images.size
    }

    class SliderAdapterVH(var itemView: View) : SliderViewAdapter.ViewHolder(itemView) {
        var imageViewBackground: SubsamplingScaleImageView = itemView.ivs_item
        init {
            imageViewBackground.maxScale = 7.0f
        }

    }
}