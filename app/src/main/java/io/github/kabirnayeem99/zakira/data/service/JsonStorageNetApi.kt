package io.github.kabirnayeem99.zakira.data.service

import io.github.kabirnayeem99.zakira.data.dto.PhraseRemoteDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import javax.inject.Inject

class JsonStorageNetApi @Inject constructor(private val client: HttpClient) {

    suspend fun getApologyArabicPhrases(): Result<List<PhraseRemoteDto>> {
        val apologyArabicPhraseResourcePath =
            "4f6d507b-2098-4521-9a04-e9af6a2182cd/da2ecc63-38c4-4f32-91fb-79e4a7e9a819"
        return getData(apologyArabicPhraseResourcePath)
    }

    private suspend fun getData(resourcePath: String): Result<List<PhraseRemoteDto>> {
        val url = "$BASE_URL/$resourcePath"

        return try {
            val response = client.get(url)

            if (response.status == HttpStatusCode.OK) {
                val data = response.body<List<PhraseRemoteDto>>()
                Result.success(data)
            } else {
                Result.failure(Exception("Failed to fetch data: ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


}

private const val BASE_URL = "https://api.jsonstorage.net/v1/json"
