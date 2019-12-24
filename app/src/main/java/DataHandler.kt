package com.anjay.notify

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import org.json.JSONArray
import org.json.JSONObject
import java.io.InputStream
import java.nio.charset.Charset


class DataHandler {

    lateinit var db: AppDatabase
    lateinit var cardDao: CardDao
    var cards = mutableListOf<Card>()
    lateinit var spe: SharedPreferences.Editor

    private fun populateFromFile(con: Context) {
        var s = con.assets.open("dummy_data.json").readTextAndClose()
        if (s == "") return
        var jo = JSONObject(s)
        var ja = jo.getJSONArray("data")
        for (i in 0 until ja.length()) {
            cardDao.insert((cardFromObject(ja.getJSONObject(i))))
        }
        cards = cardDao.getAll().toMutableList()
    }

    fun getCards(l: Long): List<Card>? {
        return null
    }
    fun getCount(): Int {
        return cards.size
    }

    fun addCard(c: Card) {
        cards.add(c)
        cardDao.insert(c)
    }

    private fun InputStream.readTextAndClose(charset: Charset = Charsets.UTF_8): String {
        return this.bufferedReader(charset).use { it.readText() }
    }


    fun cardFromObject(jo: JSONObject): Card {
        Log.d("sb", jo.toString())
        var c = Card()
        c.head = jo.getString("h")
        c.body = jo.getString("body")
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


    private constructor(con: Context) {
        Thread(Runnable {
            db = AppDatabase.getAppDataBase(context = con)!!
            cardDao = db.cardDao()
            if (cardDao.getAll().isEmpty()) {                                                       //first install

                spe = con.getSharedPreferences("prefs", 0).edit()
                spe.putLong("FirstInstallTime", System.currentTimeMillis())
                spe.putLong("DataUpdateTime", 0)

                populateFromFile(con)
            } else {
                cards = cardDao.getAll().toMutableList()
            }
        }).start()

    }


    companion object {
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