package com.anjay.notify

import android.os.Bundle
import android.os.Handler
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import org.json.JSONObject


class MainActivity : AppCompatActivity() {
    lateinit var dh: DataHandler
    lateinit var mrv: RecyclerView
    lateinit var dum: TextView
    lateinit var data: JSONObject
    lateinit var srv: SwipeRefreshLayout


    lateinit var h: Handler
    var card_count = 0
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        h = Handler(mainLooper)

        dum = findViewById(R.id.dum)
        mrv = findViewById(R.id.mrv)
        dh = DataHandler.getInstance(baseContext)!!
        srv = findViewById(R.id.swiperefresh)
        srv.setOnRefreshListener {
        }


        var iv_launch = findViewById<Button>(R.id.iv_launch)
        iv_launch.setOnClickListener {
            supportActionBar?.hide()
            var v = ImageViewer(baseContext)
            addContentView(
                v,
                ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            )
        }


        mrv.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        mrv.adapter = CardAdapter(dh.cardDao.getAll(), baseContext)
        mrv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1) && dy > 0) {
                    Toast.makeText(baseContext, "Last", Toast.LENGTH_LONG).show()

                }
            }
        })
        dum.text = "Welcome"

        var t = Thread(Runnable {
            while (true) {
                if (card_count != dh.getCount()) {
                    h.post {
                        mrv.adapter?.notifyDataSetChanged()
                    }
                    card_count = dh.getCount()
                }
                Thread.sleep(200)
            }
        })
        t.start()


    }



}