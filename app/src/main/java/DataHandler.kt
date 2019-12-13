package com.anjay.notify

import android.content.Context
import android.util.Log
import android.widget.Toast
import org.json.JSONArray
import org.json.JSONObject
import java.io.InputStream
import java.nio.charset.Charset


class DataHandler {

    lateinit var db: AppDatabase
    lateinit var cardDao: CardDao
    var size = 0

    fun getCards(): List<Card> {
        return cardDao.getAll()
    }

    private fun populateFromFile(con: Context) {
        var s = con.assets.open("dummy_data.json").readTextAndClose()
        if (s == "") return
        var jo = JSONObject(s)
        var ja = jo.getJSONArray("data")
        for (i in 0 until ja.length()) {
            cardDao.insert((cardFromObject(ja.getJSONObject(i))))
        }
    }

    fun getCards(l: Long): List<Card>? {
        return null
    }
    fun getCount(): Int {
        return size
    }

    private fun InputStream.readTextAndClose(charset: Charset = Charsets.UTF_8): String {
        return this.bufferedReader(charset).use { it.readText() }
    }

    private constructor(con: Context) {
        db = AppDatabase.getAppDataBase(context = con)!!
        cardDao = db.cardDao()
        if (cardDao.getAll().isEmpty()) {                                                                   //New Install
            Toast.makeText(con, "Welcome", Toast.LENGTH_SHORT).show()
            populateFromFile(con)
        }
        size = cardDao.getAll().size
    }

    companion object {

        fun cardFromObject(jo: JSONObject): Card {
            Log.d("sb", jo.toString())
            var c = Card()
            c.head = jo.getString("h")
            c.body = jo.getString("summary")
            c.timestamp = jo.getLong("time")
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

        fun cardsFromArray(ja: JSONArray): MutableList<Card> {
            var arr = mutableListOf<Card>()
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