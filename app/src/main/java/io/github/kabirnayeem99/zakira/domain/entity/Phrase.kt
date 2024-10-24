package io.github.kabirnayeem99.zakira.domain.entity

data class Phrase(
    val id: Long = -1,
    val arabicWithTashkeel: String = "",
    val arabicWithOutTashkeel: String = "",
    val meaning: String = "",
    val categoryId: Long = -1,
    val isFavourite: Boolean = false,
    val doesNeedFurtherPractice: Boolean = false,
    val isRead: Boolean = false,
    val isCustom: Boolean = false,
)