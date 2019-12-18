package com.anjay.notify

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "data")
data class Card(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "timestamp") var timestamp: Long = 0,
    @ColumnInfo(name = "head") var head: String = "",
    @ColumnInfo(name = "body") var body: String = "",
    @ColumnInfo(name = "images") var images: MutableList<String> = mutableListOf(),
    @ColumnInfo(name = "videos") var videos: MutableList<String> = mutableListOf()
)