package com.anjay.notify

import android.os.Bundle
import android.os.Handler
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlin.system.exitProcess


class MainActivity : AppCompatActivity() {
    lateinit var dh: DataHandler
    lateinit var mrv: RecyclerView
    lateinit var srv: SwipeRefreshLayout
    var imageViewOnScreen = false
    lateinit var iv: ImageViewer
    lateinit var h: Handler
    var cardCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        STARTTIME = System.currentTimeMillis()
        Thread(Runnable {
            FacebookHandler.getPosts(0)
        }).start()

        lg("Content View Initialized")

        h = Handler(mainLooper)
        iv = ImageViewer(baseContext, mutableListOf("a", "b"))
        mrv = findViewById(R.id.mrv)
        dh = DataHandler.getInstance(baseContext)!!

        lg("DH Initialized")

        srv = findViewById(R.id.swiperefresh)
        srv.setOnRefreshListener {
        }

        lg("IV Listner ")
        mrv.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        mrv.adapter = CardAdapter(dh.cards, this)
        lg("MRV Adapter Added")

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
                if (cardCount != dh.getCount()) {
                    (mrv.adapter as CardAdapter).items = dh.cards
                    h.post { mrv.adapter?.notifyDataSetChanged() }
                    cardCount = dh.getCount()
                    Thread.sleep(200)
                }
            }
        }).start()

        lg("End: ")
    }

    override fun onBackPressed() {
        if (imageViewOnScreen) {
            imageViewOnScreen = false
            (iv.parent as ViewGroup).removeView(iv)
            supportActionBar?.show()
        } else {
            finish()
            exitProcess(0)
        }
    }
}