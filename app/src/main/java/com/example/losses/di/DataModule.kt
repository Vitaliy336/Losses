package com.example.losses.di

import android.content.Context
import android.net.ConnectivityManager
import androidx.room.Room
import com.example.losses.executors.MainThreadExecutor
import com.example.losses.executors.WorkerThreadExecutor
import com.example.losses.repository.LossesRepository
import com.example.losses.repository.local.LocalDataSource
import com.example.losses.repository.local.room.StatisticsDatabase
import com.example.losses.repository.remote.ApiClient
import com.example.losses.repository.remote.RemoteDataSource
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule {

    @Provides
    @Singleton
    fun provideApiClient(): ApiClient {
        return ApiClient()
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(
        apiClient: ApiClient,
        workerThreadExecutor: WorkerThreadExecutor
    ): RemoteDataSource {
        return RemoteDataSource(apiClient, workerThreadExecutor)
    }

    @Provides
    @Singleton
    fun provideConnectivityManager(appContext: Context): ConnectivityManager {
        return appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    @Provides
    @Singleton
    fun provideDataBase(appContext: Context): StatisticsDatabase {
        return Room.databaseBuilder(appContext,
            StatisticsDatabase::class.java,
            "statistics-database")
            .build()
    }

    @Provides
    @Singleton
    fun provideWorkerThreadExecutor(): WorkerThreadExecutor {
        return WorkerThreadExecutor()
    }

    @Provides
    @Singleton
    fun provideMainThreadExecutor(): MainThreadExecutor {
        return MainThreadExecutor()
    }

    @Provides
    @Singleton
    fun provideLocalDataSource(
        statisticsDatabase: StatisticsDatabase,
        workerThreadExecutor: WorkerThreadExecutor
    ): LocalDataSource {
        return LocalDataSource(statisticsDatabase, workerThreadExecutor)
    }

    @Provides
    @Singleton
    fun provideRepository(
        remoteDataSource: RemoteDataSource,
        localDataSource: LocalDataSource,
        connectivityManager: ConnectivityManager,
        mainThreadExecutor: MainThreadExecutor
    ): LossesRepository {
        return LossesRepository(
            remoteDataSource,
            localDataSource,
            connectivityManager,
            mainThreadExecutor)
    }

}