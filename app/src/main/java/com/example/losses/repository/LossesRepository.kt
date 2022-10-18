package com.example.losses.repository

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.MutableLiveData
import com.example.losses.R
import com.example.losses.executors.MainThreadExecutor
import com.example.losses.repository.local.LocalDataSource
import com.example.losses.repository.model.Statistics
import com.example.losses.repository.remote.RemoteDataSource
import java.time.LocalDate
import javax.inject.Inject

class LossesRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val connectivityManager: ConnectivityManager,
    private val mainThreadExecutor: MainThreadExecutor
) {

    fun getStatistics(
        statisticsLivedata: MutableLiveData<Statistics>,
        messageLiveData: MutableLiveData<Int>,
        date: String
    ) {
        localDataSource.getLocalStatisticsByDate(date, object : StatisticsLocalCallback {
            override fun onContentRetrieved(statistics: Statistics) {
                mainThreadExecutor.execute(Runnable {
                    statisticsLivedata.value = statistics
                })
            }

            override fun onFailed(throwable: Throwable) {
                loadDataFromRemote(statisticsLivedata, messageLiveData, date)
            }
        })
    }

    fun loadDataFromRemote(
        statisticsLivedata: MutableLiveData<Statistics>,
        messageLiveData: MutableLiveData<Int>,
        date: String
    ) {
        if(isNetworkAvailable()) {
            val currentDate = LocalDate.parse(date)
            val isToday = !currentDate.isBefore(LocalDate.now())
            val isFuture = currentDate.isAfter(LocalDate.now())
            if(isFuture) {
                messageLiveData.postValue(R.string.not_data_yet)
            } else {
                remoteDataSource.loadStatisticsByDate(date,
                    isToday,
                    object : StatisticsRemoteCallback {
                        override fun onContentFetched(statistics: Statistics) {
                            localDataSource.cacheStatistics(statistics)
                            statisticsLivedata.value = statistics
                        }

                        override fun onContentFailed(throwable: Throwable) {
                            messageLiveData.value = R.string.no_data_error
                        }
                    })
            }
        } else {
            messageLiveData.postValue(R.string.network_error)
        }
    }


    private fun isNetworkAvailable(): Boolean {
        val networkCapabilities: NetworkCapabilities? =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return if(networkCapabilities != null) {
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
        } else {
            false
        }
    }

    interface StatisticsRemoteCallback {
        fun onContentFetched(statistics: Statistics)
        fun onContentFailed(throwable: Throwable)
    }

    interface StatisticsLocalCallback {
        fun onContentRetrieved(statistics: Statistics)
        fun onFailed(throwable: Throwable)
    }
}