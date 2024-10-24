package io.github.kabirnayeem99.zakira.presentation.common

sealed class UiEvent {
    data class UserMessage(val message: String) : UiEvent()
    data object CloseAndGoBack : UiEvent()
    data object GoToHome : UiEvent()
    data object RestartApp: UiEvent()
}