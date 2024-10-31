package io.github.kabirnayeem99.zakira.presentation.ui.search

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material.icons.automirrored.rounded.ManageSearch
import androidx.compose.material.icons.rounded.Category
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.intl.LocaleList
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.kabirnayeem99.zakira.presentation.ui.common.BaseComposeScreen
import io.github.kabirnayeem99.zakira.presentation.ui.common.HandleUiEvents
import io.github.kabirnayeem99.zakira.presentation.ui.home.PhraseListItem
import kotlinx.serialization.Serializable

@Serializable
object SearchScreenNavigation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchScreenViewModel,
    onBack: () -> Unit = {},
) {

    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val phraseListState = rememberLazyListState()
    val context = LocalContext.current
    val clipboardManager =
        remember { context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager }



    BaseComposeScreen(
        topBar = {
            TopAppBar(title = { Text("بحث عن العبارات") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                ),
                navigationIcon = {
                    IconButton(onClick = {
                        onBack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBackIos,
                            contentDescription = null,
                        )
                    }
                })
        },
        snackbarHostState = snackbarHostState,
    ) { scaffoldPadding ->
        LazyColumn(
            state = phraseListState,
            modifier = Modifier
                .padding(scaffoldPadding)
                .padding(horizontal = 12.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Spacer(Modifier.height(20.dp))
                val query = remember { mutableStateOf("") }
                OutlinedTextField(
                    value = query.value,
                    onValueChange = { q -> query.value = q },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth(),
                    keyboardActions = KeyboardActions {
                        viewModel.onSearch(query.value)
                    },
                    textStyle = MaterialTheme.typography.labelLarge.copy(
                        textDirection = TextDirection.Rtl,
                        fontSize = 18.sp,
                    ),
                    keyboardOptions = KeyboardOptions(
                        hintLocales = LocaleList("en-US, ar"),
                        autoCorrectEnabled = true,
                        keyboardType = KeyboardType.Text,
                    ),
                    suffix = {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = null,
                            modifier = Modifier
                                .size(24.dp)
                                .clickable {
                                    query.value = ""
                                    viewModel.onSearch(query.value)
                                },
                        )

                    },
                    singleLine = true,
                    maxLines = 1,
                    prefix = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ManageSearch,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    },
                )
                Spacer(Modifier.height(20.dp))
            }
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(uiState.value.categories) { category ->
                        FilterChip(
                            selected = category.isSelected,
                            modifier = Modifier
                                .padding(4.dp)
                                .height(32.dp),
                            onClick = {
                                viewModel.toggleCategorySelection(category)
                            },
                            label = {
                                Text(
                                    category.name,
                                    maxLines = 1,
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        textDirection = TextDirection.Rtl,
                                        fontSize = 16.sp,
                                        lineHeight = 38.sp,
                                    ),
                                    modifier = Modifier.padding(horizontal = 6.dp)
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Rounded.Category,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            },
                        )
                    }
                }
                Spacer(Modifier.height(12.dp))
            }

            items(uiState.value.phrases, key = { p -> p.id }) { phrase ->
                PhraseListItem(phrase = phrase,
                    onFavClick = { viewModel.toggleFavourite(phrase) },
                    modifier = Modifier.animateItem(),
                    onDelete = {},
                    onMarkAsRead = { viewModel.toggleRead(phrase) },
                    onCopyText = {
                        val clip = ClipData.newPlainText(
                            "تم النسخ: " + phrase.arabicWithTashkeel,
                            "${phrase.arabicWithTashkeel}\n${phrase.meaning}"
                        )
                        clipboardManager.setPrimaryClip(clip)
                    })
            }
        }
    }

    HandleUiEvents(viewModel.uiEvent, snackbarHostState)
}
