package io.github.kabirnayeem99.zakira.presentation.favourite

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.pm.ActivityInfo
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material.icons.rounded.BrowseGallery
import androidx.compose.material.icons.rounded.Category
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.kabirnayeem99.zakira.presentation.common.BaseComposeScreen
import io.github.kabirnayeem99.zakira.presentation.common.HandleUiEvents
import io.github.kabirnayeem99.zakira.presentation.common.config.MontserratFontFamily
import io.github.kabirnayeem99.zakira.presentation.home.PhraseListItem
import kotlinx.serialization.Serializable

@Serializable
object FavouriteScreenNavigation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavouriteScreen(
    viewModel: FavouriteViewModel,
    onBack: () -> Unit = {},
) {
    val context = LocalContext.current
    val clipboardManager =
        remember { context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager }

    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val phraseListState = rememberLazyListState()
    val pagerState =
        rememberPagerState { if (uiState.value.phrases.size < 30) uiState.value.phrases.size else 30 }

    BackHandler(uiState.value.isSlideShow) {
        viewModel.toggleSlideShow(false)
    }

    val activity = (LocalContext.current as Activity)
    LaunchedEffect(uiState.value.isSlideShow) {
        if (uiState.value.isSlideShow) {
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        } else {
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
    }

    BaseComposeScreen(
        topBar = {
            if (!uiState.value.isSlideShow) {
                TopAppBar(
                    title = { Text("بحث عن العبارات") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                    ),
                    actions = {
                        IconButton(onClick = {
                            viewModel.toggleSlideShow(true)
                        }) {
                            Icon(
                                imageVector = Icons.Rounded.BrowseGallery,
                                contentDescription = null,
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            onBack()
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.ArrowBackIos,
                                contentDescription = null,
                            )
                        }
                    },
                )
            }
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

            if (!uiState.value.isSlideShow) {
                items(uiState.value.phrases, key = { p -> p.id }) { phrase ->
                    PhraseListItem(
                        phrase = phrase,
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
                        }
                    )
                }
            } else {
                item {
                    HorizontalPager(
                        state = pagerState,
                        contentPadding = PaddingValues(horizontal = 8.dp),
                        key = { page ->
                            uiState.value.phrases[page].id
                        },
                    ) { page ->
                        val phrase = uiState.value.phrases[page]

                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                                .fillMaxHeight(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(
                                phrase.arabicWithTashkeel,
                                style = MaterialTheme.typography.labelLarge.copy(
                                    textDirection = TextDirection.Rtl,
                                    fontSize = 26.sp,
                                    lineHeight = 44.sp,
                                    textAlign = TextAlign.Center,
                                ),
                            )
                            Spacer(Modifier.height(20.dp))
                            Text(
                                phrase.meaning,
                                fontFamily = MontserratFontFamily,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    textDirection = TextDirection.Ltr,
                                    fontSize = 18.sp,
                                    textAlign = TextAlign.Center,
                                    lineHeight = 18.sp,
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    HandleUiEvents(viewModel.uiEvent, snackbarHostState)
}
