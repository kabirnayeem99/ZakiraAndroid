package io.github.kabirnayeem99.zakira.data.datasource

import io.github.kabirnayeem99.zakira.data.dto.PhraseRemoteDto
import io.github.kabirnayeem99.zakira.data.service.JsonStorageNetApi
import javax.inject.Inject

class PhraseRemoteDataSource @Inject constructor(private val jsonStorageNetApi: JsonStorageNetApi) {
    suspend fun getApologyArabicPhrases(): Result<List<PhraseRemoteDto>> {
        return jsonStorageNetApi.getApologyArabicPhrases()
    }
}