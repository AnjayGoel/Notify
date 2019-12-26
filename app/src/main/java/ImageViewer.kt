package com.anjay.notify

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.smarteist.autoimageslider.IndicatorAnimations
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView

class ImageViewer : FrameLayout {
    var iv: SliderView

    constructor(con: Context, images: MutableList<String>) : super(con) {
        var mv = LayoutInflater.from(con).inflate(R.layout.image_viewer, null)
        iv = mv.findViewById(R.id.slider)
        iv.sliderAdapter = SliderImageAdapter(con, images)

        iv.setIndicatorAnimation(IndicatorAnimations.WORM)
        iv.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
        iv.autoCycleDirection = SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH
        iv.indicatorSelectedColor = Color.WHITE
        iv.indicatorUnselectedColor = Color.GRAY
        addView(mv)
    }

    fun setImages(images: MutableList<String>) {
        (iv.sliderAdapter as SliderImageAdapter).images = images
        iv.sliderAdapter.notifyDataSetChanged()
    }

}