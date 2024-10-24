package io.github.kabirnayeem99.zakira.core.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.kabirnayeem99.zakira.data.repository.DefaultPhraseRepository
import io.github.kabirnayeem99.zakira.domain.repository.PhraseRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindPhraseRepository(phraseRepository: DefaultPhraseRepository): PhraseRepository
}