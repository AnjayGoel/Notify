package com.anjay.notify.room

import androidx.room.TypeConverter

class RoomTypeConverter {
    @TypeConverter
    fun strFromList(l: List<String>): String {
        return l.joinToString(separator = "-|-") { it -> it }
    }

    @TypeConverter
    fun listFromStr(s: String): List<String> {
        return s.split("-|-")
    }
}