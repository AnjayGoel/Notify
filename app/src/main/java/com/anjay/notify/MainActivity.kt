package com.anjay.notify

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import org.json.JSONObject


class MainActivity : AppCompatActivity() {
    lateinit var dh: DataHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        var dum = findViewById<TextView>(R.id.dum)
        var mrv = findViewById<RecyclerView>(R.id.mrv)
        var data = JSONObject(assets.open("dummy_data.json").bufferedReader().readText()).getJSONArray("data")
        dh = DataHandler.getInstance(baseContext)!!

        var iv_launch = findViewById<Button>(R.id.iv_launch)
        iv_launch.setOnClickListener {
            supportActionBar?.hide()
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
        dum.text = DataHandler.cardFromString(data[0].toString()).head
    }
}