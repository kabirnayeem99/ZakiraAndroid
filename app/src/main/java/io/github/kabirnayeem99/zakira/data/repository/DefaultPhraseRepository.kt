package io.github.kabirnayeem99.zakira.data.repository

import io.github.kabirnayeem99.zakira.core.Resource
import io.github.kabirnayeem99.zakira.data.datasource.PhraseLocalDataSource
import io.github.kabirnayeem99.zakira.data.flowResource
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
import javax.inject.Inject

class DefaultPhraseRepository @Inject constructor(
    private val phraseLocalDataSource: PhraseLocalDataSource,
) : PhraseRepository {

    override fun getPhrases(
        page: Int,
        categoryIds: List<Long>,
        onlyFavourites: Boolean
    ): Flow<Resource<List<Phrase>>> {
        return flowResource {
            val phrasesDto = if (onlyFavourites) phraseLocalDataSource.getOnlyFavouritesPhrases(
                categoryIds,
                page
            ) else phraseLocalDataSource.getPhrases(categoryIds, page)
            val phrases = phrasesDto.toPhrases()
            phrases
        }
    }

    private val categoriesCache = mutableListOf<Category>()

    override fun addPhrase(
        arabicText: String, meaning: String, categoryName: String,
    ): Flow<Resource<Phrase>> = flowResource {
        val category = parseCategoryName(categoryName)

        val alreadyExists = phraseLocalDataSource.existsByArabicOrMeaning(arabicText, meaning)

        if (alreadyExists) {
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
        var category = categoriesCache.find { it.name.equals(categoryName, ignoreCase = true) }

        if (category == null) {
            category = Category(name = categoryName)
            val categoryId = phraseLocalDataSource.addCategory(category.toCategoryRoomEntity())
            category = category.copy(id = categoryId)
        }
        return category
    }

    override fun deletePhrase(id: Long): Flow<Resource<Phrase>> = flowResource {
        val deletedPhraseDto = phraseLocalDataSource.deletePhrase(id)
        deletedPhraseDto.toPhrase()
    }


    override fun searchPhrases(
        query: String,
        page: Int,
        categoryIds: List<Long>
    ): Flow<Resource<List<Phrase>>> = flowResource {
        if (query.isNotBlank()) {
            val phrasesDto = phraseLocalDataSource.searchPhrases(query, page, categoryIds)
            android.util.Log.i(TAG, "searchPhrases: for $query results $phrasesDto")
            phrasesDto.toPhrases()
        } else {
            throw Exception("Query is empty.")
        }
    }

    override fun getCategories(): Flow<Resource<List<Category>>> = flowResource {
        val categoriesDto = phraseLocalDataSource.getCategories()
        val categories = categoriesDto.toCategories()

        if (categories.isNotEmpty()) {
            categoriesCache.clear()
            categoriesCache.addAll(categories)
        }

        categories.ifEmpty { categoriesCache }
    }

    override fun toggleRead(phrase: Phrase) = flowResource {
        phraseLocalDataSource.toggleRead(phrase.id)
        phrase.copy(isRead = !phrase.isRead)
    }

    override fun toggleFavourite(phrase: Phrase) = flowResource {
        phraseLocalDataSource.toggleFavourite(phrase.id)
        phrase.copy(isFavourite = !phrase.isFavourite)
    }

    override fun updatePhrase(id: Long, arabic: String, meaning: String): Flow<Resource<Phrase>> {
        return flowResource {
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