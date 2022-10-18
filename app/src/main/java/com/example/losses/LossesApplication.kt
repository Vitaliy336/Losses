package com.example.losses

import android.app.Application
import com.example.losses.di.*

open class LossesApplication : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .dataModule(DataModule())
            .viewModule(ViewModule())
            .build()
    }
}