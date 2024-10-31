package io.github.kabirnayeem99.zakira.presentation.ui.quickreview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.core.util.lruCache

private val colorCache = lruCache<Float, Color>(30)


@Composable
fun rememberInterpolatedColor(value: Float): Color {
    require(value in 0.0f..1.0f) { "Value must be between 0.0 and 1.0" }

    val pastelRed = Color(0xFFFFB6C1)
    val pastelYellow = Color(0xFFFFEBA0)
    val pastelGreen = Color(0xFF6FE67A)

    return remember(value) {
        var color = colorCache[value]
        if (color != null) color
        else {
            when {
                value < 0.5f -> {
                    val fraction = value * 2
                    color = interpolateColor(pastelRed, pastelYellow, fraction)
                }

                else -> {
                    val fraction = (value - 0.5f) * 2
                    color = interpolateColor(pastelYellow, pastelGreen, fraction)
                }
            }
            colorCache.put(value, color)
            color
        }
    }
}

private fun interpolateColor(colorA: Color, colorB: Color, fraction: Float): Color {
    val red = (colorA.red * (1 - fraction) + colorB.red * fraction).coerceIn(0f, 1f)
    val green = (colorA.green * (1 - fraction) + colorB.green * fraction).coerceIn(0f, 1f)
    val blue = (colorA.blue * (1 - fraction) + colorB.blue * fraction).coerceIn(0f, 1f)

    return Color(red, green, blue)
}
