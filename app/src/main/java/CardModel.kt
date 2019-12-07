package com.anjay.notify

class CardModel(
    var head: String = "None",
    var summary: String = "None",
    var images: MutableList<String> = mutableListOf(),
    var videos: MutableList<String> = mutableListOf(),
    var time: Long = 0
)

