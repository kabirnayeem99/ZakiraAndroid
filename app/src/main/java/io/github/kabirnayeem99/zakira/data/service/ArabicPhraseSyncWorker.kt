package io.github.kabirnayeem99.zakira.data.service

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import io.github.kabirnayeem99.zakira.core.Resource
import io.github.kabirnayeem99.zakira.domain.repository.PhraseRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.flow.map

@HiltWorker
class ArabicPhraseSyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val phraseRepository: PhraseRepository,
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        return phraseRepository.syncPhrases().map { resource ->
            Log.i(TAG, "doWork: resource -> $resource")
            when (resource) {
                is Resource.Error -> Result.failure()
                is Resource.Failed -> Result.failure()
                is Resource.Success -> Result.success()
                else -> null
            }
        }.filterNotNull().lastOrNull() ?: Result.failure()
    }
}

private const val TAG = "ArabicPhraseSyncWorker"