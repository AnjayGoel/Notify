package com.anjay.notify

import android.content.Context
import android.widget.Toast
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.File
import java.io.FileReader


class DataHandler {


    private var dataFile: File
    private var entries = mutableListOf<Card>()

    private fun addEntriesString(s: String) {
        var ja = JSONArray(s)
        for (i in 0..ja.length()) {
            entries.add(cardFromString(ja.getString(i)))
        }
    }

    fun getCount(): Int {
        return entries.size
    }


    private constructor(con: Context) {
        dataFile = File(con.getExternalFilesDir(null), "data.json")
        if (!dataFile.exists()) {
            Toast.makeText(con, "Welcome", Toast.LENGTH_SHORT).show()
            dataFile.createNewFile()
        } else {
            var sb = StringBuilder()
            var inr = BufferedReader(FileReader(dataFile))
            for (i in inr.readLines()) sb.append(i)
            var s = sb.toString()
            if (!s.equals("")) {
                addEntriesString(s)
            }
        }
    }

    companion object {

        fun cardFromString(s: String): Card {
            var jo = JSONObject(s)
            var c = Card()
            c.head = jo.getString("h")
            c.summary = jo.getString("text")

            var images = jo.getJSONArray("images")


            for (i in 0 until images.length()) {
                c.images.add(images.getString(i))
            }

            return c
        }


        private var instance: DataHandler? = null
        fun getInstance(con: Context): DataHandler? {
            if (instance == null) {
                instance = DataHandler(con)
                return instance
            }
            return instance
        }
    }
}