package io.github.kabirnayeem99.zakira.presentation.ui.common.config

//import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.GoogleFont
import io.github.kabirnayeem99.zakira.R

val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val MontserratFontFamily = FontFamily(
    androidx.compose.ui.text.googlefonts.Font(
        googleFont = GoogleFont("Montserrat"),
        fontProvider = provider,
    )
)

//val NotoNaskhArabicFamily = FontFamily(
//    Font(
//        googleFont = GoogleFont("Noto Naskh Arabic"),
//        fontProvider = provider,
//    )
//)
//
//val ScheherazadeNewFontFamily = FontFamily(
//    Font(
//        googleFont = GoogleFont("Scheherazade New"),
//        fontProvider = provider,
//    )
//)

val KfgpqFontFamily = FontFamily(
    Font(R.font.kfgpq, FontWeight.Light),
    Font(R.font.kfgpq, FontWeight.Normal),
    Font(R.font.kfgqpc_bold, FontWeight.SemiBold),
    Font(R.font.kfgqpc_bold, FontWeight.Bold)
)

//val AmiriFontFamily = FontFamily(
//    Font(
//        googleFont = GoogleFont("Amiri Quran"),
//        fontProvider = provider,
//    )
//)
//
//val AmiriQuranFontFamily = FontFamily(
//    Font(
//        googleFont = GoogleFont("Amiri"),
//        fontProvider = provider,
//    )
//)
//
//val LateefFontFamily = FontFamily(
//    Font(
//        googleFont = GoogleFont("Lateef"),
//        fontProvider = provider,
//    )
//)


val baseline = Typography()

val defaultFontFamily = KfgpqFontFamily

val AppTypography = Typography(
    displayLarge = baseline.displayLarge.copy(fontFamily = defaultFontFamily),
    displayMedium = baseline.displayMedium.copy(fontFamily = defaultFontFamily),
    displaySmall = baseline.displaySmall.copy(fontFamily = defaultFontFamily),
    headlineLarge = baseline.headlineLarge.copy(fontFamily = defaultFontFamily),
    headlineMedium = baseline.headlineMedium.copy(fontFamily = defaultFontFamily),
    headlineSmall = baseline.headlineSmall.copy(fontFamily = defaultFontFamily),
    titleLarge = baseline.titleLarge.copy(fontFamily = defaultFontFamily),
    titleMedium = baseline.titleMedium.copy(fontFamily = defaultFontFamily),
    titleSmall = baseline.titleSmall.copy(fontFamily = defaultFontFamily),
    bodyLarge = baseline.bodyLarge.copy(fontFamily = defaultFontFamily),
    bodyMedium = baseline.bodyMedium.copy(fontFamily = defaultFontFamily),
    bodySmall = baseline.bodySmall.copy(fontFamily = defaultFontFamily),
    labelLarge = baseline.labelLarge.copy(fontFamily = defaultFontFamily),
    labelMedium = baseline.labelMedium.copy(fontFamily = defaultFontFamily),
    labelSmall = baseline.labelSmall.copy(fontFamily = defaultFontFamily),
)

