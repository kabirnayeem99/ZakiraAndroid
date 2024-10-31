package io.github.kabirnayeem99.zakira.presentation.ui.common.flipanimation


import androidx.compose.ui.graphics.Shape

internal sealed class PageFlapType(val shape: Shape) {
    data object Top : io.github.kabirnayeem99.zakira.presentation.ui.common.flipanimation.PageFlapType(
        io.github.kabirnayeem99.zakira.presentation.ui.common.flipanimation.TopShape
    )
    data object Bottom : io.github.kabirnayeem99.zakira.presentation.ui.common.flipanimation.PageFlapType(
        io.github.kabirnayeem99.zakira.presentation.ui.common.flipanimation.BottomShape
    )
    data object Left : io.github.kabirnayeem99.zakira.presentation.ui.common.flipanimation.PageFlapType(
        io.github.kabirnayeem99.zakira.presentation.ui.common.flipanimation.LeftShape
    )
    data object Right : io.github.kabirnayeem99.zakira.presentation.ui.common.flipanimation.PageFlapType(
        io.github.kabirnayeem99.zakira.presentation.ui.common.flipanimation.RightShape
    )
}