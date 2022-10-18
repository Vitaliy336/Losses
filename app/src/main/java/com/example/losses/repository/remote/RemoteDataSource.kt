package com.example.losses.repository.remote

import com.example.losses.executors.WorkerThreadExecutor
import com.example.losses.repository.LossesRepository
import com.example.losses.repository.model.ApiResponse
import com.example.losses.repository.model.Statistics
import retrofit2.Call
import retrofit2.Callback
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val apiClient: ApiClient,
    private val workerThreadExecutor: WorkerThreadExecutor
) {

    fun loadStatisticsByDate(
        date: String,
        isToday: Boolean,
        statisticsRemoteCallback: LossesRepository.StatisticsRemoteCallback
    ) {
        workerThreadExecutor.execute(Runnable {
            val callback = object : Callback<ApiResponse> {
                override fun onFailure(
                    call: Call<ApiResponse>,
                    t: Throwable
                ) {
                    statisticsRemoteCallback.onContentFailed(t)
                }

                override fun onResponse(
                    call: Call<ApiResponse>,
                    apiResponse: retrofit2.Response<ApiResponse>
                ) {
                    val body: ApiResponse? = apiResponse.body()
                    if(body != null) {
                        val statItem = Statistics(
                            date,
                            body.data.day,
                            body.data.stats,
                            body.data.increase
                        )
                        statisticsRemoteCallback.onContentFetched(statItem)
                    } else {
                        statisticsRemoteCallback.onContentFailed(Throwable("No data"))
                    }
                }
            }

            if(isToday) {
                apiClient.apiService.getLatest().enqueue(callback)
            } else {
                apiClient.apiService.getStatsByDate(date).enqueue(callback)
            }
        })
    }
}