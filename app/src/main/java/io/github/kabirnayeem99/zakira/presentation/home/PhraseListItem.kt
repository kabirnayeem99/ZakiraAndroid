package io.github.kabirnayeem99.zakira.presentation.home


import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.kabirnayeem99.zakira.domain.entity.Phrase
import io.github.kabirnayeem99.zakira.presentation.common.config.MontserratFontFamily


@Composable
fun PhraseListItem(
    phrase: Phrase,
    modifier: Modifier = Modifier,
    onFavClick: () -> Unit,
    onMarkAsRead: () -> Unit,
    onDelete: () -> Unit,
    onCopyText: () -> Unit = {},
) {
    val dismissState = rememberSwipeToDismissBoxState()

    LaunchedEffect(dismissState.currentValue) {
        when (dismissState.currentValue) {
            SwipeToDismissBoxValue.StartToEnd -> {
                dismissState.snapTo(SwipeToDismissBoxValue.Settled)
                onMarkAsRead()
            }

            SwipeToDismissBoxValue.EndToStart -> {
                dismissState.snapTo(SwipeToDismissBoxValue.Settled)
                onDelete()
            }

            else -> Unit
        }

    }
    OutlinedCard(
        modifier = modifier.pointerInput(Unit) {
            detectTapGestures(onLongPress = {
                onCopyText()
            })
        },
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(
            if (phrase.isRead) 1.5.dp else 1.dp,
            if (phrase.isRead) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.outlineVariant
        ),
    ) {
        SwipeToDismissBox(
            state = dismissState,
            enableDismissFromEndToStart = phrase.isCustom,
            backgroundContent = {
                val backgroundColor by animateColorAsState(
                    when (dismissState.targetValue) {
                        SwipeToDismissBoxValue.Settled -> MaterialTheme.colorScheme.background
                        SwipeToDismissBoxValue.StartToEnd -> MaterialTheme.colorScheme.primaryContainer
                        SwipeToDismissBoxValue.EndToStart -> MaterialTheme.colorScheme.errorContainer
                    },
                    label = "background_color",
                )
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(backgroundColor)
                )
            },
        ) {
            ListItem(
                modifier = Modifier.clip(shape = RoundedCornerShape(8.dp)),
                colors = ListItemDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.background,
                    headlineColor = if (phrase.isRead) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onBackground,
                    supportingColor = MaterialTheme.colorScheme.onBackground.copy(0.75F),
                ),
                headlineContent = {
                    Text(
                        phrase.arabicWithTashkeel,
                        style = MaterialTheme.typography.labelLarge.copy(
                            textDirection = TextDirection.Rtl,
                            fontWeight = if (phrase.isRead) FontWeight.SemiBold else FontWeight.Normal,
                            fontSize = 18.sp,
                            lineHeight = 44.sp,
                        ),
                    )
                },
                supportingContent = {
                    Text(
                        phrase.meaning,
                        fontFamily = MontserratFontFamily,
                        style = MaterialTheme.typography.labelMedium.copy(
                            textDirection = TextDirection.Ltr,
                            fontSize = 14.sp,
                            fontWeight = if (phrase.isRead) FontWeight.SemiBold else FontWeight.Normal,
                            textAlign = TextAlign.Start,
                            lineHeight = 18.sp,
                        )
                    )
                },
                trailingContent = {
                    IconButton(onClick = onFavClick) {
                        Icon(
                            imageVector = if (phrase.isFavourite) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                            contentDescription = null,
                            tint = if (phrase.isFavourite) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onBackground,
                        )
                    }
                },
            )
        }
    }
}
