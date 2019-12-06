package com.anjay.notify

import org.json.*

class Card(var head: String = "None", var summary: String = "None") {
}

fun cardFromString(s: String): Card {
    var jo = JSONObject(s)
    var c = Card()
    c.head = jo.getString("h")
    c.summary = jo.getString("text")
    return c
}