package io.github.kabirnayeem99.zakira.data.mapper

import io.github.kabirnayeem99.zakira.data.dto.PhraseRoomEntity
import io.github.kabirnayeem99.zakira.domain.entity.Phrase

fun List<PhraseRoomEntity>.toPhrases(): List<Phrase> {
    return map { it.toPhrase() }
}

fun PhraseRoomEntity.toPhrase(): Phrase {
    return Phrase(
        id = id,
        arabicWithTashkeel = arabic,
        arabicWithOutTashkeel = arabicWithoutTashkeel,
        meaning = meaning,
        isRead = isRead == 1,
        isFavourite = isFavourite,
        isCustom = isCustom == 1,
    )
}

fun Phrase.toRoomEntity(): PhraseRoomEntity {
    return if (id > 0) {
        PhraseRoomEntity(
            id = id,
            arabic = arabicWithTashkeel,
            meaning = meaning,
            categoryId = categoryId,
            arabicWithoutTashkeel = removeTashkeel(arabicWithOutTashkeel),
        )
    } else {
        PhraseRoomEntity(
            arabic = arabicWithTashkeel,
            arabicWithoutTashkeel = removeTashkeel(arabicWithOutTashkeel),
            meaning = meaning,
            categoryId = categoryId,
        )
    }
}

fun removeTashkeel(text: String): String {
    val tashkeelRegex =
        "[\\u0610-\\u061A\\u064B-\\u065F\\u06D6-\\u06DC\\u06DF-\\u06E8\\u06EA-\\u06ED]".toRegex()
    return text.replace(tashkeelRegex, "")
}