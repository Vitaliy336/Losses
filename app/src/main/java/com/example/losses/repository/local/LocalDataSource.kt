package com.example.losses.repository.local

import com.example.losses.executors.WorkerThreadExecutor
import com.example.losses.repository.LossesRepository
import com.example.losses.repository.model.Statistics
import com.example.losses.repository.local.room.StatisticsDatabase
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val statisticsDatabase: StatisticsDatabase,
    private val workerThreadExecutor: WorkerThreadExecutor,
) {

    fun getLocalStatisticsByDate(date: String, callback: LossesRepository.StatisticsLocalCallback ) {
        workerThreadExecutor.execute(Runnable {
            val statistics: Statistics? = statisticsDatabase.statisticsDao().getStatisticsByaDate(date)
            if (statistics != null) {
                callback.onContentRetrieved(statistics)
            } else {
                callback.onFailed(Throwable("no cached content for $date"))
            }
        })

    }

    fun cacheStatistics(statistics: Statistics) {
        workerThreadExecutor.execute(Runnable {
            statisticsDatabase.statisticsDao().insert(statistics)
        })
    }
}