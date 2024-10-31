@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package io.github.kabirnayeem99.zakira.presentation.ui.home

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForwardIos
import androidx.compose.material.icons.automirrored.rounded.ManageSearch
import androidx.compose.material.icons.automirrored.rounded.MenuOpen
import androidx.compose.material.icons.rounded.Category
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.kabirnayeem99.zakira.R
import io.github.kabirnayeem99.zakira.presentation.ui.common.BaseComposeScreen
import io.github.kabirnayeem99.zakira.presentation.ui.common.HandleUiEvents
import io.github.kabirnayeem99.zakira.presentation.ui.common.LastItemPaginatingView
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
object HomeScreenNavigation

@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = hiltViewModel(),
    onNavigateToSearchScreen: () -> Unit = {},
    onNavigateToFavouriteScreen: () -> Unit = {},
) {

    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val phraseListState = rememberLazyListState()
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val context = LocalContext.current
    val clipboardManager =
        remember { context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager }



    BackHandler(drawerState.isOpen) {
        scope.launch {
            drawerState.close()
        }
    }

    ModalNavigationDrawer(drawerState = drawerState, drawerContent = {
        ModalDrawerSheet(
            drawerContainerColor = MaterialTheme.colorScheme.background,
            drawerContentColor = MaterialTheme.colorScheme.onBackground.copy(0.2F),
            modifier = Modifier.padding(horizontal = 4.dp)
        ) {

            var isExpanded by remember { mutableStateOf(false) }
            LazyColumn(
                modifier = Modifier.padding(4.dp), contentPadding = PaddingValues(4.dp)
            ) {
                item {
                    Text(
                        stringResource(R.string.app_name), modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.labelLarge.copy(
                            textDirection = TextDirection.Rtl,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 44.sp,
                            color = MaterialTheme.colorScheme.primary,
                        ),
                    )
                }
                item {
                    HorizontalDivider()
                    Spacer(Modifier.height(12.dp))
                }
                item {
                    NavigationDrawerItem(
                        badge = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.ArrowForwardIos,
                                modifier = Modifier.size(18.dp),
                                contentDescription = null
                            )
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Rounded.Favorite,
                                modifier = Modifier.size(18.dp),
                                contentDescription = null
                            )
                        },
                        shape = RoundedCornerShape(8.dp),
                        label = {
                            Text(
                                text = "العبارات المفضلة",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    textDirection = TextDirection.Rtl,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    lineHeight = 44.sp,
                                ),
                            )
                        },
                        selected = false,
                        onClick = {
                            onNavigateToFavouriteScreen()
                        },
                    )
                    Spacer(Modifier.height(12.dp))
                }
                item {
                    NavigationDrawerItem(
                        badge = {
                            Icon(
                                imageVector = if (isExpanded) Icons.Rounded.KeyboardArrowUp else Icons.Rounded.KeyboardArrowDown,
                                contentDescription = null
                            )
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Rounded.Category,
                                modifier = Modifier.size(18.dp),
                                contentDescription = null
                            )
                        },
                        shape = RoundedCornerShape(8.dp),
                        label = {
                            Text(
                                text = "قائمة الفئات",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    textDirection = TextDirection.Rtl,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    lineHeight = 44.sp,
                                ),
                            )
                        },
                        selected = isExpanded,
                        onClick = { isExpanded = !isExpanded },
                    )
                }
                if (isExpanded) {
                    items(uiState.value.categories) { category ->
                        NavigationDrawerItem(
                            modifier = Modifier.animateItem(),
                            shape = RoundedCornerShape(8.dp),
                            label = {
                                Text(
                                    text = category.name,
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        textDirection = TextDirection.Rtl,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        lineHeight = 34.sp,
                                    ),
                                )
                            },
                            selected = false,
                            onClick = { isExpanded = false },
                        )
                    }
                }
            }
        }
    }) {
        BaseComposeScreen(
            snackbarHostState = snackbarHostState,
            topBar = {
                TopAppBar(
                    title = { Text("عبارات عربية شائعة") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                    ),
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                drawerState.open()
                            }
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.MenuOpen,
                                contentDescription = null
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            onNavigateToSearchScreen()
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.ManageSearch,
                                contentDescription = null,
                            )
                        }
                    },
                )
            },
            floatingActionButton = {
                AddNewPhraseFloatingActionButton(
                    visible = phraseListState.lastScrolledBackward,
                    onClick = { viewModel.toggleAddNewPhraseBottomSheet(true) },
                )
            },
            floatingActionButtonPosition = FabPosition.Center,
        ) { scaffoldPadding ->
            LazyColumn(
                state = phraseListState,
                modifier = Modifier
                    .padding(scaffoldPadding)
                    .padding(horizontal = 12.dp),
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
                    LastItemPaginatingView(
                        items = uiState.value.phrases,
                        currentItem = phrase,
                        onReachingAtEnd = { viewModel.fetchPhrases(refresh = false) },
                        getItemId = { p -> p.id.toString() },
                        loading = uiState.value.isLoading,
                    )
                }
            }

            AddNewPhraseBottomSheet(
                visible = uiState.value.showAddNewPhraseBottomSheet,
                sheetState = sheetState,
                onAddClick = { arabic, meaning, category ->
                    scope.launch {
                        sheetState.hide()
                        viewModel.addNewPhrase(arabic, meaning, category)
                    }
                },
                onDismiss = {
                    scope.launch {
                        sheetState.hide()
                        viewModel.toggleAddNewPhraseBottomSheet(false)
                    }
                },
            )
        }
    }

    HandleUiEvents(uiEvents = viewModel.uiEvent, snackbarHostState = snackbarHostState)

}
