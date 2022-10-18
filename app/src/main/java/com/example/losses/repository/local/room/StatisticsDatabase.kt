package com.example.losses.repository.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.losses.repository.local.Convertor
import com.example.losses.repository.model.Statistics

@Database(entities = [Statistics::class], version = 1)
@TypeConverters(Convertor::class)
abstract class StatisticsDatabase : RoomDatabase() {
    abstract fun statisticsDao(): StatisticsDao
}