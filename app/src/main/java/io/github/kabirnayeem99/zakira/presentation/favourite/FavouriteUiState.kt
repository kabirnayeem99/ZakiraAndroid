package io.github.kabirnayeem99.zakira.presentation.favourite

import io.github.kabirnayeem99.zakira.domain.entity.Category
import io.github.kabirnayeem99.zakira.domain.entity.Phrase

data class FavouriteUiState(
    val phrases: List<Phrase> = emptyList(),
    val isLoading: Boolean = false,
    val isSlideShow: Boolean = false,
    val categories: List<Category> = emptyList(),
)