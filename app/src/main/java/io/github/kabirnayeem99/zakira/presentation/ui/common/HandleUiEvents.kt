package io.github.kabirnayeem99.zakira.presentation.ui.common

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HandleUiEvents(
    uiEvents: SharedFlow<UiEvent>,
    snackbarHostState: SnackbarHostState,
) {
    LaunchedEffect(Unit) {
        uiEvents.collectLatest { uiEvent ->
            when (uiEvent) {
                is UiEvent.UserMessage -> {
                    snackbarHostState.showSnackbar(uiEvent.message)
                }

                else -> Unit
            }
        }
    }
}