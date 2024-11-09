package io.github.kabirnayeem99.zakira.presentation.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kabirnayeem99.zakira.core.Resource
import io.github.kabirnayeem99.zakira.domain.entity.Category
import io.github.kabirnayeem99.zakira.domain.entity.Phrase
import io.github.kabirnayeem99.zakira.domain.repository.PhraseRepository
import io.github.kabirnayeem99.zakira.domain.usecase.PhraseInputValidationUseCase
import io.github.kabirnayeem99.zakira.presentation.ui.common.UiEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel
@Inject constructor(
    private val phraseRepository: PhraseRepository,
    private val phraseInputValidation: PhraseInputValidationUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UiEvent>(
        extraBufferCapacity = 5, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val uiEvent = _uiEvent.asSharedFlow()

    private var fetchPhrasesJob: Job? = null

    private var currentPhrasePage = 1

    init {
        viewModelScope.launch {
            fetchCategories()
            delay(500L)
            fetchPhrases()
        }
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
            fetchPhrases(refresh = true)
        }
    }

    private var toggleReadJob: Job? = null
    fun toggleRead(phrase: Phrase) {
        toggleReadJob?.cancel()
        toggleReadJob = viewModelScope.launch(Dispatchers.IO) {
            phraseRepository.toggleRead(phrase).collect { resource ->
                handleResource(resource) { phrase ->
                    _uiState.update { us ->
                        val phrases = us.phrases.toMutableList()
                        val phraseIndex = phrases.indexOfFirst { it.id == phrase.id }
                        phrases[phraseIndex] = phrase
                        us.copy(phrases = phrases)
                    }
                }
            }
        }
    }

    private var toggleFavouriteJob: Job? = null

    fun toggleFavourite(phrase: Phrase) {
        toggleFavouriteJob?.cancel()
        toggleFavouriteJob = viewModelScope.launch(Dispatchers.IO) {
            phraseRepository.toggleFavourite(phrase).collect { resource ->
                handleResource(resource) { phrase ->
                    _uiState.update { us ->
                        val phrases = us.phrases.toMutableList()
                        val phraseIndex = phrases.indexOfFirst { it.id == phrase.id }
                        phrases[phraseIndex] = phrase
                        us.copy(phrases = phrases)
                    }
                }
            }
        }
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

    fun fetchPhrases(refresh: Boolean = true) {
        fetchPhrasesJob?.cancel()
        fetchPhrasesJob = viewModelScope.launch(Dispatchers.IO) {
            phraseRepository.getPhrases(
                if (refresh) 1 else ++currentPhrasePage, categoryIds = getSelectedCategoryIds()
            ).collect { resource ->
                handleResource(resource) { phrases ->
                    if (refresh) {
                        _uiState.update { us -> us.copy(phrases = phrases) }
                    } else {
                        val currentPhrases = uiState.value.phrases.toMutableList()
                        currentPhrases.addAll(phrases)
                        _uiState.update { us -> us.copy(phrases = currentPhrases.distinctBy { it.id }) }
                    }
                }
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

    private fun toggleLoading(loading: Boolean) {
        _uiState.update { us -> us.copy(isLoading = loading) }
    }


    private var addNewPhraseJob: Job? = null

    fun addNewPhrase(arabic: String, meaning: String, category: String) {
        addNewPhraseJob?.cancel()
        addNewPhraseJob = viewModelScope.launch(Dispatchers.IO) {
            toggleAddNewPhraseBottomSheet(false)
            phraseInputValidation(
                arabicText = arabic, meaning = meaning, category = category
            ).also { (valid, error) ->
                if (valid) {
                    phraseRepository.addPhrase(
                        arabicText = arabic,
                        meaning = meaning,
                        categoryName = category,
                    ).collect { resource ->
                        handleResource(resource) { phrase ->
                            showUserMessage("العبارة المضافة: " + phrase.arabicWithTashkeel)
                            fetchPhrases()
                        }
                    }
                } else {
                    showUserMessage(error)
                }
            }
        }
    }

    fun toggleAddNewPhraseBottomSheet(toggle: Boolean) {
        _uiState.update { us -> us.copy(showAddNewPhraseBottomSheet = toggle) }
    }
}

private const val TAG = "HomeScreenViewModel"