package io.github.kabirnayeem99.zakira.presentation.ui.quickreview

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kabirnayeem99.zakira.core.Resource
import io.github.kabirnayeem99.zakira.domain.usecase.GenerateQuickOverviewPhrases
import io.github.kabirnayeem99.zakira.presentation.ui.common.UiEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuickReviewViewModel @Inject constructor(
    private val generateQuickOverviewPhrases: GenerateQuickOverviewPhrases,
) : ViewModel() {

    private val _uiState = MutableStateFlow(QuickReviewUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UiEvent>(
        extraBufferCapacity = 5, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        fetchQuickOverviewPhrases()
    }

    private fun fetchQuickOverviewPhrases() {
        viewModelScope.launch(Dispatchers.IO) {
            generateQuickOverviewPhrases().collect { resource ->
                Log.i(TAG, "fetchQuickOverviewPhrases: resource $resource")
                handleResource(resource) { phrases ->
                    _uiState.update { us -> us.copy(phrases = phrases) }
                }
            }
        }
    }

    private suspend fun <T> handleResource(
        resource: Resource<T>,
        onDataFound: suspend (T) -> Unit,
    ) {
        when (resource) {
            is Resource.Error -> {

                showUserMessage(resource.message)
            }

            is Resource.Failed -> {
                showUserMessage(resource.message)
            }

            is Resource.Loading -> {
                toggleLoading(true)
            }

            is Resource.Success -> {
                toggleLoading(false)
                val data = resource.data
                if (data != null) onDataFound(data)
            }
        }
    }

    private fun showUserMessage(message: String?) {
        toggleLoading(false)
        if (message.isNullOrBlank()) return
        viewModelScope.launch { _uiEvent.emit(UiEvent.UserMessage(message)) }
    }

    private fun toggleLoading(loading: Boolean) {
        _uiState.update { us -> us.copy(isLoading = loading) }
    }
}

private const val TAG = "QuickReviewViewModel"