package io.github.kabirnayeem99.zakira.presentation.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kabirnayeem99.zakira.core.Resource
import io.github.kabirnayeem99.zakira.domain.entity.Category
import io.github.kabirnayeem99.zakira.domain.entity.Phrase
import io.github.kabirnayeem99.zakira.domain.repository.PhraseRepository
import io.github.kabirnayeem99.zakira.presentation.common.UiEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchScreenViewModel @Inject constructor(
    private val phraseRepository: PhraseRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(SearchScreenUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UiEvent>(
        extraBufferCapacity = 5, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        fetchCategories()
    }

    private var isSearching = false
    private var lastQuery = ""

    private var toggleReadJob: Job? = null
    fun toggleRead(phrase: Phrase) {
        toggleReadJob?.cancel()
        toggleReadJob = viewModelScope.launch(Dispatchers.IO) {
            phraseRepository.toggleRead(phrase).collect { resource ->
                handleResource(resource) {
                    onSearch(lastQuery)
                }
            }
        }
    }

    private var toggleFavouriteJob: Job? = null

    fun toggleFavourite(phrase: Phrase) {
        toggleFavouriteJob?.cancel()
        toggleFavouriteJob = viewModelScope.launch(Dispatchers.IO) {
            phraseRepository.toggleFavourite(phrase).collect { resource ->
                handleResource(resource) {
                    onSearch(lastQuery)
                }
            }
        }
    }

    private var onSearchJob: Job? = null
    fun onSearch(query: String) {
        onSearchJob?.cancel()
        onSearchJob = viewModelScope.launch(Dispatchers.IO) {
            if (query.isNotBlank()) {
                val categoryIds = getSelectedCategoryIds()
                isSearching = true
                lastQuery = query
                _uiState.update { us -> us.copy(phrases = emptyList()) }
                phraseRepository.searchPhrases(query = query, categoryIds = categoryIds)
                    .collect { resource ->
                        handleResource(resource) { phrases ->
                            _uiState.update { it.copy(phrases = phrases) }
                        }
                    }
            } else {
                isSearching = false
                _uiState.update { us -> us.copy(phrases = emptyList()) }
            }
        }
    }

    private fun getSelectedCategoryIds() =
        _uiState.value.categories.filter { it.isSelected }.ifEmpty { _uiState.value.categories }
            .map { it.id }


    private suspend fun <T> handleResource(
        resource: Resource<T>,
        onDataFound: suspend (T) -> Unit,
    ) {
        when (resource) {
            is Resource.Error -> {
                Log.e(TAG, "handleResource:  Error: ${resource.message}")
                showUserMessage(resource.message)
            }

            is Resource.Failed -> {
                Log.e(TAG, "handleResource:  Failed: ${resource.message}")
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

    init {
        fetchCategories()
    }

    private var fetchCategoriesJob: Job? = null

    private fun fetchCategories() {
        fetchCategoriesJob?.cancel()
        fetchCategoriesJob = viewModelScope.launch(Dispatchers.IO) {
            phraseRepository.getCategories().collect { resource ->
                handleResource(resource) { categories ->
                    _uiState.update { us -> us.copy(categories = categories) }
                }
            }
        }
    }

    private fun toggleLoading(loading: Boolean) {
        _uiState.update { us -> us.copy(isLoading = loading) }
    }

    private var toggleCategorySelectionJob: Job? = null

    fun toggleCategorySelection(category: Category) {
        toggleCategorySelectionJob?.cancel()
        toggleCategorySelectionJob = viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { us ->
                us.copy(categories = us.categories.map { c ->
                    if (c.id == category.id) c.copy(
                        isSelected = !c.isSelected
                    ) else c
                })
            }
            onSearch(lastQuery)
        }
    }


}

private const val TAG = "SearchScreenViewModel"