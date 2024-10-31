package io.github.kabirnayeem99.zakira.domain.usecase

import io.github.kabirnayeem99.zakira.core.Resource
import io.github.kabirnayeem99.zakira.domain.entity.Phrase
import io.github.kabirnayeem99.zakira.domain.repository.PhraseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class GenerateQuickOverviewPhrases @Inject constructor(
    private val phraseRepository: PhraseRepository,
) {
    operator fun invoke(): Flow<Resource<List<Phrase>>> = flow {
        emitAll(phraseRepository.getRandomPhrases())
//        val recentPhrases = phraseRepository.getRecentPhrases().lastOrNull()
//        if (recentPhrases is Resource.Success && !recentPhrases.data.isNullOrEmpty()) {
//            emit(recentPhrases)
//        } else {
//            val currentTime = System.currentTimeMillis()
//            val random = Random(currentTime)
//            val fallbackSource = if (random.nextBoolean()) {
//                phraseRepository.getFavouritePhrases()
//            } else {
//                phraseRepository.getRandomPhrases()
//            }
//            emitAll(fallbackSource)
//        }
    }.onStart { emit(Resource.Loading()) }.catch { e ->
        emit(Resource.Error(e.message))
    }
}