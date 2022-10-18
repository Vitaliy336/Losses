package com.example.losses.executors

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import javax.inject.Inject

class MainThreadExecutor @Inject constructor() : Executor {
    private val mainThreadHandler: Handler = Handler(Looper.getMainLooper())

    override fun execute(runnable: Runnable) {
        this.mainThreadHandler.post(runnable)
    }
}