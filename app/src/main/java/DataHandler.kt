package com.anjay.notify

import android.content.Context
import android.content.SharedPreferences
import java.io.InputStream
import java.nio.charset.Charset


class DataHandler {

    lateinit var db: AppDatabase
    lateinit var cardDao: CardDao
    var cards = mutableListOf<Card>()
    lateinit var spe: SharedPreferences.Editor

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

    private constructor(con: Context) {
        Thread(Runnable {
            db = AppDatabase.getAppDataBase(context = con)!!
            cardDao = db.cardDao()
            if (cardDao.getAll().isEmpty()) {                                                       //first install
                spe = con.getSharedPreferences("prefs", 0).edit()
                spe.putLong("FirstInstallTime", System.currentTimeMillis())
                spe.putLong("DataUpdateTime", 0)
                cards = FacebookHandler.getPosts(0)
                cardDao.insertArray(cards)
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