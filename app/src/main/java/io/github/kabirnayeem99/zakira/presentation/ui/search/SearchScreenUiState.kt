package io.github.kabirnayeem99.zakira.presentation.ui.search

import io.github.kabirnayeem99.zakira.domain.entity.Category
import io.github.kabirnayeem99.zakira.domain.entity.Phrase

data class SearchScreenUiState(
    val phrases: List<Phrase> = emptyList(),
    val isLoading: Boolean = false,
    val categories: List<Category> = emptyList(),
)
