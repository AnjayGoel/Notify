package com.anjay.notify

import android.content.Context
import android.util.Log
import android.widget.Toast
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.nio.charset.Charset


class DataHandler {


    private var dataFile: File
    private var entries = mutableListOf<CardModel>()
    private var lastUpdate: Long = 0

    fun getCards(): MutableList<CardModel> {
        return entries
    }

    private fun populateFromFile() {
        var s = FileInputStream(dataFile).readTextAndClose()
        if (s == "") return
        var jo = JSONObject(s)
        lastUpdate = jo.getLong("lastUpdate")
        var ja = jo.getJSONArray("data")
        for (i in 0 until ja.length()) {
            entries.add(cardFromObject(ja.getJSONObject(i)))
        }
    }

    fun getCount(): Int {
        return entries.size
    }

    private fun InputStream.readTextAndClose(charset: Charset = Charsets.UTF_8): String {
        return this.bufferedReader(charset).use { it.readText() }
    }

    private constructor(con: Context) {
        dataFile = File(con.getExternalFilesDir(null), "data.json")
        if (!dataFile.exists()) {                                                                   //New Install
            Toast.makeText(con, "Welcome", Toast.LENGTH_SHORT).show()

            con.assets.open("dummy_data.json").copyTo(FileOutputStream(dataFile))
            con.assets.open("dummy.jpg")
                .copyTo(FileOutputStream(File(con.getExternalFilesDir(null), "dummy.jpg")))


        }
        populateFromFile()
    }

    companion object {

        fun cardFromObject(jo: JSONObject): CardModel {
            Log.d("sb", jo.toString())
            var c = CardModel()
            c.head = jo.getString("h")
            c.summary = jo.getString("summary")
            c.time = jo.getLong("time")
            var jai: JSONArray = jo.getJSONArray("images")
            var jav: JSONArray = jo.getJSONArray("videos")
            var images = mutableListOf<String>()
            var videos = mutableListOf<String>()

            for (i in 0 until jai.length()) {
                images.add(jai.getString(i))
            }
            for (i in 0 until jav.length()) {
                videos.add(jav.getString(i))
            }
            c.images = images
            c.videos = videos
            return c
        }

        fun cardsFromArray(ja: JSONArray): MutableList<CardModel> {
            var arr = mutableListOf<CardModel>()
            for (i in 0 until ja.length()) {
                arr.add(cardFromObject(ja.getJSONObject(i)))
            }
            return arr
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