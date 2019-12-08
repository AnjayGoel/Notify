package com.anjay.notify

import java.text.SimpleDateFormat
import java.util.*

fun timeFromString(l: Long): String {
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