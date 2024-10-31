package io.github.kabirnayeem99.zakira.presentation.ui.quickreview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.kabirnayeem99.zakira.presentation.ui.common.BaseComposeScreen
import io.github.kabirnayeem99.zakira.presentation.ui.common.config.BlackEerie
import io.github.kabirnayeem99.zakira.presentation.ui.common.config.PastelGreen
import io.github.kabirnayeem99.zakira.presentation.ui.common.flipanimation.FlipPager
import io.github.kabirnayeem99.zakira.presentation.ui.common.flipanimation.FlipPagerOrientation
import kotlinx.serialization.Serializable

@Serializable
object QuickReviewScreenNavigation

@Composable
fun QuickReviewScreen(viewModel: QuickReviewViewModel) {

    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    val phrases = uiState.value.phrases
    val pagerState = rememberPagerState { phrases.size }


    BaseComposeScreen(
        containerColor = PastelGreen
    ) { scaffoldPadding ->
        FlipPager(
            state = pagerState,
            orientation = FlipPagerOrientation.Vertical,
        ) { index ->

            val phrase = phrases[index]
            val color = rememberInterpolatedColor(phrase.learnScore)

            Column(
                modifier = Modifier
                    .background(color)
                    .padding(scaffoldPadding)
                    .padding(24.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    phrase.arabicWithTashkeel,
                    style = MaterialTheme.typography.labelMedium.copy(
                        textDirection = TextDirection.Rtl,
                        color = BlackEerie,
                        fontSize = 32.sp,
                        lineHeight = (32 * 3).sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                    )

                )
                Spacer(Modifier.height(45.dp))
                Text(
                    phrase.meaning,
                    style = MaterialTheme.typography.labelMedium.copy(
                        textAlign = TextAlign.Center,
                        textDirection = TextDirection.Ltr,
                        lineHeight = (22 * 1.5).sp,
                        color = BlackEerie.copy(0.75F),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                )

            }
        }
    }
}


private const val TAG = "QuickReviewScreen"