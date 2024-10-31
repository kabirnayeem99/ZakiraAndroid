package io.github.kabirnayeem99.zakira.core.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.kabirnayeem99.zakira.data.service.JsonStorageNetApi
import io.github.kabirnayeem99.zakira.data.service.db.CategoryRoomDao
import io.github.kabirnayeem99.zakira.data.service.db.LocalDatabase
import io.github.kabirnayeem99.zakira.data.service.db.PhraseRoomDao
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {
    @Provides
    @Singleton
    fun provideLocalDatabase(@ApplicationContext context: Context) =
        LocalDatabase.createLocalDatabase(context)

    @Provides
    @Singleton
    fun providePhraseDao(localDatabase: LocalDatabase): PhraseRoomDao = localDatabase.phraseDao()

    @Provides
    @Singleton
    fun provideCategoryDao(localDatabase: LocalDatabase): CategoryRoomDao =
        localDatabase.categoryDao()

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
        val client = HttpClient(OkHttp) {
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.HEADERS
            }
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        isLenient = true
                        encodeDefaults = true
                    },
                )
            }
        }
        return client
    }

    @Provides
    @Singleton
    fun provideJsonStorageNetApi(client: HttpClient): JsonStorageNetApi {
        return JsonStorageNetApi(client)
    }
}