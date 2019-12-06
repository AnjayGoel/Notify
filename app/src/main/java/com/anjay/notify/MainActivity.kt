package com.anjay.notify

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.json.*
import kotlin.io.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.view.LayoutInflater
import android.widget.*
import androidx.annotation.ContentView
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        var dum = findViewById<TextView>(R.id.dum)
        var mrv = findViewById<RecyclerView>(R.id.mrv)
        var data = JSONObject(assets.open("dummy_data.json").bufferedReader().readText()).getJSONArray("data")


        var iv_launch = findViewById<Button>(R.id.iv_launch)
        iv_launch.setOnClickListener {
            actionBar?.hide()
            setContentView(R.layout.image_viewer)
            var iv = findViewById<SubsamplingScaleImageView>(R.id.imageView)
            iv.setImage(ImageSource.resource(R.drawable.dummy))
        }



        var linearLayoutManager : LinearLayoutManager = LinearLayoutManager(
            this, // Context
            RecyclerView.VERTICAL, // Orientation
            false // Reverse layout
        )
        mrv.layoutManager = linearLayoutManager
        mrv.adapter = CardAdapter(data,baseContext)
        mrv.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1)&&dy>0) {
                    Toast.makeText(baseContext, "Last", Toast.LENGTH_LONG).show()

                }
            }
        })
        dum.text = cardFromString(data[0].toString()).head
    }
}