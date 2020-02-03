package com.anjay.notify

import android.content.Context
import android.util.Log
import java.io.File
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*


val TAG = "SilverBug"
var STARTTIME: Long = 0
var LASTTIME: Long = 0

fun lg(s: String) {
    Log.d(
        TAG,
        "TT:" + (System.currentTimeMillis() - STARTTIME) + " Diff:" + (System.currentTimeMillis() - LASTTIME) + "--> " + s
    )
    LASTTIME = System.currentTimeMillis()
}

fun timestampFromString(s: String): Long { //time in seconds
    return SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(
        s.replace('T', ' ').replace(
            "+0000",
            ""
        )
    ).time / 1000
}

fun timeToString(l: Long): String { //time in seconds
    var now = Calendar.getInstance()
    var t = Calendar.getInstance()


    t.time = Date(l * 1000)
    if (t.get(Calendar.YEAR) == now.get(Calendar.YEAR)) {
        if (t.get(Calendar.DAY_OF_YEAR) == now.get(Calendar.DAY_OF_YEAR)) {
            val dateFormat = SimpleDateFormat("HH:mm aa")
            return "Today at " + dateFormat.format(t.time)
        } else if (t.get(Calendar.DAY_OF_YEAR) == now.get(Calendar.DAY_OF_YEAR) - 1) {
            val dateFormat = SimpleDateFormat("HH:mm aa")
            return "Yesterday at " + dateFormat.format(t.time)
        } else {
            val dateFormat = SimpleDateFormat("dd MMMM")
            val dateFormat2 = SimpleDateFormat("HH:mm aa")
            return dateFormat.format(t.time) + " at " + dateFormat2.format(t.time)
        }
    } else {
        val dateFormat = SimpleDateFormat("dd MMMM yyyy")
        val dateFormat2 = SimpleDateFormat("HH:mm aa")
        return dateFormat.format(t.time) + " at " + dateFormat2.format(t.time)
    }
}

fun saveURL(url: String, con: Context): String? {
    try {
        val url = URL(url)
        val connection = url.openConnection()
        var fileStream = File(con.getExternalFilesDir(null), url.file).outputStream()
        var inpStream = url.openStream()

        connection.connect()
        inpStream.copyTo(fileStream)
        fileStream.close()
        inpStream.close()


    } catch (e: Exception) {
        Log.e("Error: ", e.message)
    }

    return null
}