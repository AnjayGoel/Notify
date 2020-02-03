package com.anjay.notify.room

import androidx.room.*

@Dao
interface CardDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(card: Card)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertArray(cards: List<Card>)

    @Delete
    fun delete(card: Card)

    @Update
    fun update(card: Card)

    @Query("SELECT * FROM data")
    fun getAll(): List<Card>

    @Query("SELECT * FROM data WHERE id = :id")
    fun getCardById(id: Long): Card

    @Query("SELECT * FROM data WHERE timestamp > :t")
    fun getCardsAfter(t: Long): List<Card>

    @Query("SELECT * FROM data WHERE timestamp < :t")
    fun getCardsBefore(t: Long): List<Card>

    @Query("SELECT MAX(timestamp) FROM data")
    fun getLastUpdate(): Long

    @Query("SELECT MIN(timestamp) FROM data")
    fun getFirstUpdate(): Long

    @Query("SELECT COUNT(id) FROM data")
    fun getCount(): Int

    @Query("SELECT COUNT(1) FROM data WHERE id = :id")
    fun has(id: Long): Boolean

    @Query("SELECT * FROM data ORDER BY timestamp DESC LIMIT :limit OFFSET :offset")
    fun get(limit: Int, offset: Int): List<Card>
}