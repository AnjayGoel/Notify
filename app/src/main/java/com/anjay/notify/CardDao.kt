package com.anjay.notify.com.anjay.notify

import androidx.room.*

@Dao
interface CardDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(card: Card)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertArray(cards: MutableList<Card>)
    @Delete
    fun delete(card: Card)

    @Update
    fun update(card: Card)

    @Query("SELECT * FROM data")
    fun getAll(): List<Card>
}