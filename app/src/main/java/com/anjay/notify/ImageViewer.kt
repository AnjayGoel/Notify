package com.anjay.notify
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.anjay.notify.adapters.SliderImageAdapter
import com.smarteist.autoimageslider.IndicatorAnimations
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import kotlinx.android.synthetic.main.slider_image_viewer.view.*

class ImageViewer : FrameLayout {
    var sliderView: SliderView

    constructor(con: Context, images: MutableList<String>) : super(con) {
        var mv = LayoutInflater.from(con).inflate(R.layout.slider_image_viewer, null)
        sliderView = mv.ssiv
        sliderView.sliderAdapter =
            SliderImageAdapter(con, images)
        sliderView.setIndicatorAnimation(IndicatorAnimations.WORM)
        sliderView.setSliderTransformAnimation(SliderAnimations.ZOOMOUTTRANSFORMATION)
        sliderView.indicatorSelectedColor = Color.WHITE
        sliderView.indicatorUnselectedColor = Color.GRAY
        addView(mv)
    }

    fun setImages(images: MutableList<String>, con: Context) {
        sliderView.sliderAdapter =
            SliderImageAdapter(con, images)
    }

}