package io.github.kabirnayeem99.zakira.presentation.ui.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
fun <T> LastItemPaginatingView(
    items: List<T>,
    currentItem: T,
    loading: Boolean,
    onReachingAtEnd: () -> Unit,
    getItemId: (T) -> String
) {
    val lastItem = items.lastOrNull() ?: return

    val lastItemId = getItemId(lastItem)
    val isItLastItem = lastItemId == getItemId(currentItem)


    LaunchedEffect(lastItemId) { if (isItLastItem) onReachingAtEnd() }

    AnimatedVisibility(visible = isItLastItem && loading) {
        CircularProgressIndicator()
    }
}