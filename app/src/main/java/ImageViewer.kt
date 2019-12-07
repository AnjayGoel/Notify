package com.anjay.notify

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView

class ImageViewer : FrameLayout {
    lateinit var mv: View
    lateinit var iv: ImageView

    constructor(con: Context) : super(con) {
        var mv = LayoutInflater.from(con).inflate(R.layout.image_viewer, null)
        var iv = mv.findViewById<SubsamplingScaleImageView>(R.id.imageView)
        iv.setImage(ImageSource.asset("dummy.jpg"))
        mv.setOnTouchListener(View.OnTouchListener { v, event ->
            (mv.parent as ViewGroup).removeViewAt(1)
            return@OnTouchListener true
        })
        addView(mv)
    }

}