package io.github.kabirnayeem99.zakira.presentation.common.config

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


private val lightColorScheme = lightColorScheme(
    primary = OliveBronze,
    onPrimary = Color.White,
    primaryContainer = OlivePesto,
    onPrimaryContainer = Color.White,
    secondary = CoffeeIrish,
    onSecondary = Color.White,
    secondaryContainer = CoffeeMatrix,
    onSecondaryContainer = Color.White,
    tertiary = GreenVerdun,
    onTertiary = Color.White,
    tertiaryContainer = GreenPacifika,
    onTertiaryContainer = Color.White,
    error = DarkRedBuccaneer,
    onError = Color.White,
    errorContainer = CoffeeMatrix,
    onErrorContainer = Color.White,
    background = WhiteBianca,
    onBackground = GreenRangoon,
    surface = WhiteSoft,
    onSurface = DeepBlackSlate,
    surfaceVariant = SilverLight,
    onSurfaceVariant = BackCapeCod,
    outline = GraySmokyMuted,
    outlineVariant = GrayCoolMuted,
    scrim = Color.Black,
    inverseSurface = CharcoalDark,
    inverseOnSurface = IceLight,
    inversePrimary = OliveLight,
    surfaceDim = GrayMistyLight,
    surfaceBright = WhiteSoft,
    surfaceContainerLowest = Color.White,
    surfaceContainerLow = BlueVeryLight,
    surfaceContainer = BlueSoftLight,
    surfaceContainerHigh = PaleBlueSoft,
    surfaceContainerHighest = GrayBlueishGeyser,
)


private val darkColorScheme = darkColorScheme(
    primary = OliveDeco,
    onPrimary = BlackDeepFir,
    primaryContainer = OliveDark,
    onPrimaryContainer = Color.Black,
    secondary = SalmonLight,
    onSecondary = MaroonDark,
    secondaryContainer = CoralPinkSoft,
    onSecondaryContainer = Color.Black,
    tertiary = YellowWarm,
    onTertiary = BrownDark,
    tertiaryContainer = OliveKhakiTertiary,
    onTertiaryContainer = Color.Black,
    error = RedLightPink,
    onError = RedDark,
    errorContainer = RedSoftContainer,
    onErrorContainer = Color.Black,
    background = GreenRangoon,
    onBackground = GrayPale,
    surface = DeepBlack,
    onSurface = WhiteBlueishPolar,
    surfaceVariant = GrayDarkSlate,
    onSurfaceVariant = GraySoft,
    outline = GraySteelOutline,
    outlineVariant = GrayDullOutline,
    scrim = Color.Black,
    inverseSurface = GrayBlueishGeyser,
    inverseOnSurface = GrayHighContainer,
    inversePrimary = OliveDarkInverseInverse,
    surfaceDim = DeepBlack,
    surfaceBright = GrayDarkBright,
    surfaceContainerLowest = BlackBunker,
    surfaceContainerLow = DeepBlackSlate,
    surfaceContainer = GrayDarkSlateContainer,
    surfaceContainerHigh = GrayHighContainer,
    surfaceContainerHighest = GrayHighestContainer,
)


@Composable
fun ZakiraLanguageMemoryTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit
) {
    val colorScheme = when {
        darkTheme -> darkColorScheme
        else -> lightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        shapes = Shapes(
            small = RoundedCornerShape(8.dp),
            medium = RoundedCornerShape(8.dp),
            large = RoundedCornerShape(8.dp)
        ),
        content = content,
    )
}

