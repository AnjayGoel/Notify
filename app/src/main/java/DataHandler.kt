package com.anjay.notify

import android.content.Context
import android.content.SharedPreferences

class DataHandler {

    var oldCount = 0
    lateinit var db: AppDatabase
    lateinit var cardDao: CardDao
    var cards = mutableListOf<Card>()
    lateinit var spe: SharedPreferences.Editor

    fun addCard(c: Card) {
        cards.add(c)
        cardDao.insert(c)
    }

    fun addCards(cl: List<Card>) {
        cardDao.insertArray(cl)
        cards.addAll(cl)
    }


    private constructor(con: Context) {
        Thread(Runnable {
            db = AppDatabase.getAppDataBase(context = con)!!
            cardDao = db.cardDao()
            if (cardDao.getAll().isEmpty()) {                                                       //first install
                spe = con.getSharedPreferences("prefs", 0).edit()
                spe.putLong("FirstInstallTime", System.currentTimeMillis())
                spe.putLong("DataUpdateTime", 0)
                var cl = FacebookHandler.getPosts(0)
                if (cl != null) {
                    addCards(cl)
                } else {
                    //TODO some error message
                }

            } else {
                cards = cardDao.getAll().toMutableList()
            }
        }).start()

    }


    companion object {
        private var instance: DataHandler? = null
        fun getInstance(con: Context): DataHandler {
            if (instance == null) {
                instance = DataHandler(con)
            }
            return instance!!
        }
    }
}