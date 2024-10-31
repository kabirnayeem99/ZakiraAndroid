package io.github.kabirnayeem99.zakira.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PhraseRemoteDto(
    @SerialName("phrase") val phrase: String? = "",
    @SerialName("meaning") val meaning: String? = "",
)