package io.github.kabirnayeem99.zakira.presentation.ui.quickreview

import io.github.kabirnayeem99.zakira.domain.entity.Category
import io.github.kabirnayeem99.zakira.domain.entity.Phrase

data class QuickReviewUiState(
    val phrases: List<Phrase> = emptyList(),
    val isLoading: Boolean = false,
    val showAddNewPhraseBottomSheet: Boolean = false,
    val categories: List<Category> = emptyList(),
)