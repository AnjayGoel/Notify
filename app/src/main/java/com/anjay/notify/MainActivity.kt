package com.anjay.notify

import android.os.Bundle
import android.os.Handler
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlin.system.exitProcess


class MainActivity : AppCompatActivity() {
    lateinit var mrv: RecyclerView
    lateinit var srv: SwipeRefreshLayout
    var dh = DataHandler.getInstance(this)
    lateinit var iv: ImageViewer
    lateinit var h: Handler

    var imageViewOnScreen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        STARTTIME = System.currentTimeMillis()

        NotificationWorker.startNewRequest(this)

        h = Handler(mainLooper)
        iv = ImageViewer(baseContext, mutableListOf("a", "b"))

        mrv = findViewById(R.id.mrv)
        mrv.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        mrv.adapter = CardAdapter(this)

        srv = findViewById(R.id.swiperefresh)
        srv.setOnRefreshListener {
            Thread(Runnable {
                var t = FacebookHandler.getLatestUpdateTime()
                if (t != dh.cardDao.getLastUpdate()) {
                    var cl = FacebookHandler.getPosts(dh.cardDao.getLastUpdate())
                    dh.addCards(cl)
                }
                srv.isRefreshing = false
            }).start()
        }

        Thread(Runnable {
            //
            while (true) {
                if (dh.oldCount != dh.cards.size) {
                    h.post {
                        dh.cards.sortByDescending { it -> it.timestamp }
                        (mrv.adapter as RecyclerView.Adapter).notifyDataSetChanged()
                        dh.oldCount = dh.cards.size
                    }
                }
            }
        }).start()
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