package com.anjay.notify

import androidx.room.*

@Dao
interface CardDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(card: Card)

    @Delete
    fun delete(card: Card)

    @Update
    fun update(card: Card)

    @Query("SELECT * FROM data")
    fun getAll(): List<Card>
}