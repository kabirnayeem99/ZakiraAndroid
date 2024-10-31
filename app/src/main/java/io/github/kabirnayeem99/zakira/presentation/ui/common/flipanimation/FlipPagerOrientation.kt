package io.github.kabirnayeem99.zakira.presentation.ui.common.flipanimation


sealed class FlipPagerOrientation {
    data object Vertical : FlipPagerOrientation()
    data object Horizontal : FlipPagerOrientation()
}