package io.github.kabirnayeem99.zakira.domain.entity

data class Category(
    val id: Long = -1,
    val name: String = "",
    val description: String = "",
    val meaning: String = "",
    val isSelected: Boolean = false,
)
