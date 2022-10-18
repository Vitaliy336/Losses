package com.example.losses.di

import androidx.lifecycle.MutableLiveData
import com.example.losses.repository.LossesRepository
import com.example.losses.repository.model.Statistics
import com.example.losses.view.ActivityViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ViewModule {

    @Provides
    @Singleton
    fun provideStatisticsLivedata():MutableLiveData<Statistics> {
        return MutableLiveData<Statistics>()
    }

    @Provides
    @Singleton
    fun provideMessageLivedata():MutableLiveData<Int> {
        return MutableLiveData<Int>()
    }

    @Provides
    @Singleton
    fun provideMainViewModel(
        statisticsLivedata: MutableLiveData<Statistics>,
        messageLiveData: MutableLiveData<Int>,
        repository: LossesRepository
    ): ActivityViewModel {
        return ActivityViewModel(statisticsLivedata, messageLiveData, repository )
    }
}