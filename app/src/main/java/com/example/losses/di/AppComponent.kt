package com.example.losses.di

import com.example.losses.view.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, DataModule::class, ViewModule::class])
interface AppComponent {
    fun inject(activity: MainActivity)
}