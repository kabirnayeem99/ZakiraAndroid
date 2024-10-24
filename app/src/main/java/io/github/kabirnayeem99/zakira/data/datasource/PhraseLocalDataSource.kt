package io.github.kabirnayeem99.zakira.data.datasource

import io.github.kabirnayeem99.zakira.data.dto.CategoryRoomEntity
import io.github.kabirnayeem99.zakira.data.dto.PhraseRoomEntity
import io.github.kabirnayeem99.zakira.data.service.db.CategoryRoomDao
import io.github.kabirnayeem99.zakira.data.service.db.PhraseRoomDao
import javax.inject.Inject

class PhraseLocalDataSource @Inject constructor(
    private val phraseRoomDao: PhraseRoomDao,
    private val categoryRoomDao: CategoryRoomDao,
) {
    suspend fun getPhrases(categoryIds: List<Long>, page: Int): List<PhraseRoomEntity> {
        return phraseRoomDao.getAll(categoryIds, (page * 10) - 10)
    }

    suspend fun getOnlyFavouritesPhrases(
        categoryIds: List<Long>,
        page: Int
    ): List<PhraseRoomEntity> {
        return phraseRoomDao.getOnlyFavouritesAll(categoryIds, (page * 10) - 10)
    }

    suspend fun addCategory(category: CategoryRoomEntity): Long {
        return categoryRoomDao.insert(category)
    }

    suspend fun toggleFavourite(phraseId: Long) = phraseRoomDao.toggleFavourite(phraseId)

    suspend fun toggleRead(phraseId: Long) = phraseRoomDao.toggleRead(phraseId)

    suspend fun addPhrase(phrase: PhraseRoomEntity): Long {
        return phraseRoomDao.insert(phrase.copy(isCustom = 0))
    }

    suspend fun deletePhrase(phraseId: Long): PhraseRoomEntity {
        val phrase = phraseRoomDao.getById(phraseId)
        phraseRoomDao.deleteById(phraseId)
        return phrase
    }

    suspend fun searchPhrases(
        searchQuery: String,
        page: Int = 1,
        categoryIds: List<Long>
    ): List<PhraseRoomEntity> {
        return phraseRoomDao.searchPhrases(searchQuery, categoryIds)
    }

    suspend fun getCategories(): List<CategoryRoomEntity> = categoryRoomDao.getAllCategories()
    suspend fun updatePhrase(phraseDto: PhraseRoomEntity) {
        phraseRoomDao.updatePhrase(phraseDto)
    }

    suspend fun existsByArabicOrMeaning(arabic: String, meaning: String) =
        phraseRoomDao.existsByArabicOrMeaning(arabic, meaning)
}