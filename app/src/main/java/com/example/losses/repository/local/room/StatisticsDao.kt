package com.example.losses.repository.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.losses.repository.model.Statistics

@Dao
interface StatisticsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(statistics: Statistics)

    @Query("SELECT * FROM statistics_table where date = :date")
    fun getStatisticsByaDate(date: String): Statistics?
}