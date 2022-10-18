package com.example.losses.executors

import java.util.concurrent.*
import javax.inject.Inject

class WorkerThreadExecutor @Inject constructor() : Executor {

    private val threadPoolExecutor: ThreadPoolExecutor

    init {
        val numberOfCores = Runtime.getRuntime().availableProcessors()
        this.threadPoolExecutor = ThreadPoolExecutor(
            numberOfCores,
            numberOfCores * 2,
            keepAliveTime,
            keepAliveTimeUnit,
            LinkedBlockingQueue(),
            WorkerThreadFactory()
        )
    }

    override fun execute(runnable: Runnable?) {
        this.threadPoolExecutor.execute(runnable)
    }

    public fun shutDownTask(runnable: Runnable) {
        this.threadPoolExecutor.remove(runnable)
    }

    companion object {
        private const val keepAliveTime: Long = 10
        private val keepAliveTimeUnit = TimeUnit.SECONDS

        class WorkerThreadFactory : ThreadFactory {
            companion object {
                const val threadName = "worker_";
            }

            private var counter = 0;

            override fun newThread(runnable: Runnable?): Thread {
                return Thread(runnable, "$threadName$counter")
            }
        }
    }
}