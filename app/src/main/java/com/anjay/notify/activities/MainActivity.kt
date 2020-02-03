package com.anjay.notify.activities

import android.os.Bundle
import android.os.Handler
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anjay.notify.*
import com.anjay.notify.adapters.CardAdapter
import com.anjay.notify.handlers.DataHandler
import com.anjay.notify.handlers.FacebookHandler
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.system.exitProcess


class MainActivity : AppCompatActivity() {
    var dh = DataHandler.getInstance(this)
    lateinit var iv: ImageViewer
    lateinit var h: Handler

    var imageViewOnScreen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        STARTTIME = System.currentTimeMillis()


        NotificationWorker.createNotificationChannel(
            this,
            NotificationManagerCompat.IMPORTANCE_HIGH,
            true,
            "Updates",
            "Post Updates"
        )
        NotificationWorker.startNewRequest(this)

        h = Handler(mainLooper)
        iv = ImageViewer(baseContext, mutableListOf("a", "b"))

        mrv.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        mrv.adapter = CardAdapter(this)

        srv.setOnRefreshListener {
            Thread(Runnable {
                var t = FacebookHandler.getLatestUpdateTime()
                if (t != dh.cardDao.getLastUpdate()) {
                    var cl = FacebookHandler.getPosts(dh.cardDao.getLastUpdate())
                    if (cl != null) {
                        dh.addCards(cl)
                    } else {
                        //TODO error message
                    }
                }
                srv.isRefreshing = false
            }).start()
        }

        Thread(Runnable {
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
            iv.setImages(mutableListOf(), this)
            (iv.parent as ViewGroup).removeView(iv)
            supportActionBar?.show()
        } else {
            finish()
            exitProcess(0)
        }
    }
}