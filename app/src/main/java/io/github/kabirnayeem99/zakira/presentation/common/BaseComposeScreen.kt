package io.github.kabirnayeem99.zakira.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun BaseComposeScreen(
    topBar: @Composable () -> Unit = {},
    showLoading: Boolean = false,
    snackbarHostState: SnackbarHostState? = null,
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    isArabic: Boolean = true,
    content: @Composable (PaddingValues) -> Unit
) {
        Scaffold(
            topBar = topBar,
            containerColor = MaterialTheme.colorScheme.background,
            floatingActionButton = floatingActionButton,
            floatingActionButtonPosition = floatingActionButtonPosition,
            snackbarHost = {
                if (snackbarHostState != null) SnackbarHost(hostState = snackbarHostState)
            },
        ) { scaffoldPadding ->
            if (showLoading) {
                Dialog(
                    onDismissRequest = {},
                    DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .width(130.dp)
                            .height(110.dp)
                            .background(
                                MaterialTheme.colorScheme.background.copy(0.75F),
                                shape = RoundedCornerShape(8.dp)
                            )
                    ) {}
                }
            }
            content(scaffoldPadding)

    }
}
