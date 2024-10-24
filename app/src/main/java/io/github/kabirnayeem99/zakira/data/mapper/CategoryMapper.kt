package io.github.kabirnayeem99.zakira.data.mapper

import io.github.kabirnayeem99.zakira.data.dto.CategoryRoomEntity
import io.github.kabirnayeem99.zakira.domain.entity.Category

fun Category.toCategoryRoomEntity(): CategoryRoomEntity {
    return CategoryRoomEntity(
        name = name, description = description.ifBlank { null },
        meaning = meaning,
    )
}

fun List<CategoryRoomEntity>.toCategories() = map { it.toCategory() }

fun CategoryRoomEntity.toCategory(): Category {
    return Category(name = name, description = description ?: "", id = id, meaning = meaning)
}