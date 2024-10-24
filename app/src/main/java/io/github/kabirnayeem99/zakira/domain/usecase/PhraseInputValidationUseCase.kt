package io.github.kabirnayeem99.zakira.domain.usecase

import javax.inject.Inject

class PhraseInputValidationUseCase @Inject constructor() {

    operator fun invoke(
        arabicText: String, meaning: String, category: String
    ): Pair<Boolean, String?> {
        // Check if Arabic and meaning text are not empty
        if (arabicText.isBlank()) {
            return false to "Arabic text cannot be empty."
        }
        if (meaning.isBlank()) {
            return false to "Meaning cannot be empty."
        }

        // Check if category is not blank or empty
        if (category.isBlank()) {
            return false to "Category cannot be blank or empty."
        }

        // Check if Arabic contains tashkeel/harakat
        val tashkeelRegex = Regex("[\\u0617-\\u061A\\u064B-\\u0652]")
        if (!tashkeelRegex.containsMatchIn(arabicText)) {
            return false to "Arabic text must contain tashkeel or harakat."
        }

        // Check if Arabic text contains only Arabic characters, diacritical marks, and allowed symbols
        val arabicOnlyRegex =
            Regex("^[\\u0600-\\u06FF\\s\\u0610-\\u061A\\u064B-\\u065F\\u06D6-\\u06DC\\u06DF-\\u06E8\\u06EA-\\u06ED;,!^&$*()]+$")
        if (!arabicText.matches(arabicOnlyRegex)) {
            return false to "Arabic text must contain only Arabic characters, diacritical marks, and allowed symbols."
        }

        // Check if meaning (in English) contains only English letters, numbers, spaces, and allowed symbols
//        val englishRegex = Regex("^[a-zA-Z0-9\\s,;.!?'\"]+\\\$&*()]+$")
//        if (!meaning.matches(englishRegex)) {
//            return false to "Meaning must be in English and may contain letters, numbers, spaces, and allowed symbols."
//        }


        // Check if Arabic text length is not excessively long (e.g., not more than 4-5 lines)
        val maxArabicLength = 150 // Rough estimate for 4-5 lines
        if (arabicText.length > maxArabicLength) {
            return false to "Arabic text is too long."
        }

        // Check if English meaning is not too long (e.g., not more than 2 lines)
        val maxMeaningLength = 100 // Rough estimate for 2 lines
        if (meaning.length > maxMeaningLength) {
            return false to "Meaning text is too long."
        }

        // Check if category contains only letters (Arabic or English)
        val categoryRegex = Regex("^[\\p{L}\\s]+$")
        if (!category.matches(categoryRegex)) {
            return false to "Category must contain only letters and no symbols or numbers."
        }

        // If all checks pass
        return true to null
    }
}
