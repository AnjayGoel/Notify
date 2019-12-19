package com.anjay.notify

import android.os.Bundle
import android.os.Handler
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout


class MainActivity : AppCompatActivity() {
    lateinit var dh: DataHandler
    lateinit var mrv: RecyclerView
    lateinit var srv: SwipeRefreshLayout
    var imageViewOnScreen = false
    lateinit var iv: ImageViewer
    lateinit var h: Handler
    var card_count = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        var t = System.currentTimeMillis()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lg("Content View Initialized " + (System.currentTimeMillis() - t))
        h = Handler(mainLooper)
        iv = ImageViewer(baseContext, mutableListOf("a", "b"))
        mrv = findViewById(R.id.mrv)
        dh = DataHandler.getInstance(baseContext)!!
        lg("DH Initialized " + (System.currentTimeMillis() - t))
        srv = findViewById(R.id.swiperefresh)
        srv.setOnRefreshListener {
        }

        var ivLaunch = findViewById<Button>(R.id.iv_launch)
        ivLaunch.setOnClickListener {
            supportActionBar?.hide()
            imageViewOnScreen = true
            addContentView(
                iv,
                ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            )
        }

        lg("IV Listner " + (System.currentTimeMillis() - t))
        mrv.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        mrv.adapter = CardAdapter(dh.cards, this)
        lg("MRV Adapter Added " + (System.currentTimeMillis() - t))
        mrv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1) && dy > 0) {
                    Toast.makeText(baseContext, "Last", Toast.LENGTH_LONG).show()
                }
            }
        })
        Thread(Runnable {
            while (true) {
                if (card_count != dh.getCount()) {
                    (mrv.adapter as CardAdapter).items = dh.cards
                    h.post { mrv.adapter?.notifyDataSetChanged() }
                    card_count = dh.getCount()
                    Thread.sleep(200)
                }
            }
        }).start()

        lg("End: " + (System.currentTimeMillis() - t))
    }

    override fun onBackPressed() {
        if (imageViewOnScreen) {
            imageViewOnScreen = false
            (iv.parent as ViewGroup).removeView(iv)
            supportActionBar?.show()
        }
    }
}