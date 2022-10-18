package com.example.losses.repository.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "statistics_table")
data class Statistics(
    @PrimaryKey val date: String,
    val day: String,
    val statisticsMap: HashMap<String, Int>,
    val increase: HashMap<String, Int>?,
)