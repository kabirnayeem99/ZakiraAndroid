package io.github.kabirnayeem99.zakira.core.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.kabirnayeem99.zakira.data.service.db.CategoryRoomDao
import io.github.kabirnayeem99.zakira.data.service.db.LocalDatabase
import io.github.kabirnayeem99.zakira.data.service.db.PhraseRoomDao
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
    fun provideCategoryDao(localDatabase: LocalDatabase): CategoryRoomDao = localDatabase.categoryDao()
}