package io.github.kabirnayeem99.zakira.presentation.service

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.kabirnayeem99.zakira.data.service.ArabicPhraseSyncWorker
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ArabicPhraseSyncScheduler @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val workManager by lazy {
        WorkManager.getInstance(context)
    }

    fun scheduleWeeklySync() {
        val weeklyRequest =
            PeriodicWorkRequestBuilder<ArabicPhraseSyncWorker>(7, TimeUnit.DAYS).build()
        workManager.enqueueUniquePeriodicWork(
            WORK_NAME, ExistingPeriodicWorkPolicy.UPDATE, weeklyRequest
        )
    }

    fun forceImmediateSync() {
        workManager.cancelUniqueWork(WORK_NAME)

        val immediateRequest = OneTimeWorkRequestBuilder<ArabicPhraseSyncWorker>().build()
        workManager.enqueue(immediateRequest)

        scheduleWeeklySync()
    }
}

const val WORK_NAME = "WeeklyArabicPhraseSync"