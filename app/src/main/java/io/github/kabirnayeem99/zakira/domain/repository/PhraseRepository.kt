package io.github.kabirnayeem99.zakira.domain.repository

import io.github.kabirnayeem99.zakira.core.Resource
import io.github.kabirnayeem99.zakira.domain.entity.Category
import io.github.kabirnayeem99.zakira.domain.entity.Phrase
import kotlinx.coroutines.flow.Flow

interface PhraseRepository {
    fun syncPhrases(): Flow<Resource<Unit>>

    fun getPhrases(
        page: Int = 1, categoryIds: List<Long>, onlyFavourites: Boolean = false
    ): Flow<Resource<List<Phrase>>>

    fun addPhrase(arabicText: String, meaning: String, categoryName: String): Flow<Resource<Phrase>>

    fun deletePhrase(id: Long): Flow<Resource<Phrase>>

    fun updatePhrase(id: Long, arabic: String, meaning: String): Flow<Resource<Phrase>>

    fun searchPhrases(
        query: String, page: Int = 1, categoryIds: List<Long>
    ): Flow<Resource<List<Phrase>>>

    fun getRandomPhrases(page: Int = 1): Flow<Resource<List<Phrase>>>

    fun getRecentPhrases(page: Int = 1): Flow<Resource<List<Phrase>>>

    fun getFavouritePhrases(page: Int = 1): Flow<Resource<List<Phrase>>>

    fun getCategories(): Flow<Resource<List<Category>>>

    fun toggleRead(phrase: Phrase): Flow<Resource<Phrase>>

    fun toggleFavourite(phrase: Phrase): Flow<Resource<Phrase>>
}