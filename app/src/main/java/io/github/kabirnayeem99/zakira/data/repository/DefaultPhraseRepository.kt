package io.github.kabirnayeem99.zakira.data.repository

import android.util.Log
import io.github.kabirnayeem99.zakira.core.Resource
import io.github.kabirnayeem99.zakira.data.asResourceFlow
import io.github.kabirnayeem99.zakira.data.asResourceFlowFromFlow
import io.github.kabirnayeem99.zakira.data.datasource.PhraseLocalDataSource
import io.github.kabirnayeem99.zakira.data.datasource.PhraseRemoteDataSource
import io.github.kabirnayeem99.zakira.data.mapper.removeTashkeel
import io.github.kabirnayeem99.zakira.data.mapper.toCategories
import io.github.kabirnayeem99.zakira.data.mapper.toCategoryRoomEntity
import io.github.kabirnayeem99.zakira.data.mapper.toPhrase
import io.github.kabirnayeem99.zakira.data.mapper.toPhrases
import io.github.kabirnayeem99.zakira.data.mapper.toRoomEntity
import io.github.kabirnayeem99.zakira.domain.entity.Category
import io.github.kabirnayeem99.zakira.domain.entity.Phrase
import io.github.kabirnayeem99.zakira.domain.repository.PhraseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DefaultPhraseRepository @Inject constructor(
    private val phraseLocalDataSource: PhraseLocalDataSource,
    private val phraseRemoteDataSource: PhraseRemoteDataSource,
) : PhraseRepository {

    override fun syncPhrases(): Flow<Resource<Unit>> {
        return asResourceFlow {
            val apologyPhrasesRemoteResult = phraseRemoteDataSource.getApologyArabicPhrases()
            apologyPhrasesRemoteResult.onSuccess { apologyPhrasesRemoteDtoList ->
                val apologyCategoryName = "ٱلِٱعْتِذَارُ وَٱلْمَغْفِرَةُ"
                apologyPhrasesRemoteDtoList.forEach { apologyPhrasesRemoteDto ->
                    val arabicText = apologyPhrasesRemoteDto.phrase ?: return@forEach
                    val meaning = apologyPhrasesRemoteDto.meaning ?: return@forEach
                    val savedPhrase = savePhrase(
                        arabicText = arabicText,
                        meaning = meaning,
                        categoryName = apologyCategoryName
                    )
                    Log.i(TAG, "syncPhrases: saved phrase -> $savedPhrase")
                }
            }.onFailure { e ->
                throw e
            }
            Unit
        }
    }

    override fun getPhrases(
        page: Int, categoryIds: List<Long>, onlyFavourites: Boolean
    ): Flow<Resource<List<Phrase>>> {
        return asResourceFlow {
            val phrasesDto = if (onlyFavourites) phraseLocalDataSource.getOnlyFavouritesPhrases(
                categoryIds, page
            ) else phraseLocalDataSource.getPhrases(categoryIds, page)
            val phrases = phrasesDto.toPhrases()
            phrases
        }
    }

    private val categoriesCache = mutableSetOf<Category>()

    override fun addPhrase(
        arabicText: String,
        meaning: String,
        categoryName: String,
    ): Flow<Resource<Phrase>> = asResourceFlow {
        savePhrase(categoryName, arabicText, meaning)
    }

    private suspend fun DefaultPhraseRepository.savePhrase(
        categoryName: String, arabicText: String, meaning: String
    ): Phrase {
        val category = parseCategoryName(categoryName)

        val alreadyExists = phraseLocalDataSource.existsByArabicOrMeaning(arabicText, meaning)

        return if (alreadyExists) {
            throw IllegalStateException("Phrases already exists.")
        } else {

            val phrase = Phrase(
                arabicWithTashkeel = arabicText,
                arabicWithOutTashkeel = removeTashkeel(arabicText),
                meaning = meaning,
                categoryId = category.id,
            )

            val phraseId = phraseLocalDataSource.addPhrase(phrase.toRoomEntity())

            if (phraseId > 0) phrase.copy(id = phraseId) else throw Exception("Failed to create phrase.")
        }
    }

    private suspend fun parseCategoryName(categoryName: String): Category {
        if (categoriesCache.isEmpty()) {
            val categories = phraseLocalDataSource.getCategories().toCategories()
            categories.forEach { c ->
                categoriesCache.add(c)
            }
            getCategories().lastOrNull()
        }
        var category = categoriesCache.find { it.name.equals(categoryName, ignoreCase = true) }

        if (category == null) {
            category = Category(name = categoryName)
            val categoryId = phraseLocalDataSource.addCategory(category.toCategoryRoomEntity())
            category = category.copy(id = categoryId)
        }
        return category
    }

    override fun deletePhrase(id: Long): Flow<Resource<Phrase>> = asResourceFlow {
        val deletedPhraseDto = phraseLocalDataSource.deletePhrase(id)
        deletedPhraseDto.toPhrase()
    }


    override fun searchPhrases(
        query: String, page: Int, categoryIds: List<Long>
    ): Flow<Resource<List<Phrase>>> = asResourceFlow {
        if (query.isNotBlank()) {
            val phrasesDto = phraseLocalDataSource.searchPhrases(query, categoryIds)
            android.util.Log.i(TAG, "searchPhrases: for $query results $phrasesDto")
            phrasesDto.toPhrases()
        } else {
            throw Exception("Query is empty.")
        }
    }

    override fun getRandomPhrases(page: Int) = asResourceFlowFromFlow {
        phraseLocalDataSource.getRandomPhrases().map { fp -> fp.toPhrases() }
    }

    override fun getRecentPhrases(page: Int) = asResourceFlowFromFlow {
        phraseLocalDataSource.getRecentPhrases().map { fp -> fp.toPhrases() }
    }

    override fun getFavouritePhrases(page: Int) = asResourceFlowFromFlow {
        phraseLocalDataSource.getFavouritePhrases().map { fp -> fp.toPhrases() }
    }

    override fun getCategories(): Flow<Resource<List<Category>>> = asResourceFlow {
        val categoriesDto = phraseLocalDataSource.getCategories()
        val categories = categoriesDto.toCategories()

        if (categories.isNotEmpty()) {
            categoriesCache.clear()
            categoriesCache.addAll(categories)
        }

        categories.ifEmpty { categoriesCache.toList() }
    }

    override fun toggleRead(phrase: Phrase) = asResourceFlow {
        phraseLocalDataSource.toggleRead(phrase.id)
        phrase.copy(isRead = !phrase.isRead)
    }

    override fun toggleFavourite(phrase: Phrase) = asResourceFlow {
        phraseLocalDataSource.toggleFavourite(phrase.id)
        phrase.copy(isFavourite = !phrase.isFavourite)
    }

    override fun updatePhrase(id: Long, arabic: String, meaning: String): Flow<Resource<Phrase>> {
        return asResourceFlow {
            val phrase = Phrase(
                id = id,
                arabicWithTashkeel = arabic,
                meaning = meaning,
                arabicWithOutTashkeel = removeTashkeel(arabic)
            )
            val phraseDto = phrase.toRoomEntity()
            phraseLocalDataSource.updatePhrase(phraseDto)
            phrase
        }
    }
}

private const val TAG = "DefaultPhraseRepository"