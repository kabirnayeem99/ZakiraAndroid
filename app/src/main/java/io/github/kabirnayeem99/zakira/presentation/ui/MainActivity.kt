package io.github.kabirnayeem99.zakira.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.zakira.presentation.service.ArabicPhraseSyncScheduler
import io.github.kabirnayeem99.zakira.presentation.ui.common.config.ZakiraLanguageMemoryTheme
import io.github.kabirnayeem99.zakira.presentation.ui.favourite.FavouriteScreen
import io.github.kabirnayeem99.zakira.presentation.ui.favourite.FavouriteScreenNavigation
import io.github.kabirnayeem99.zakira.presentation.ui.favourite.FavouriteViewModel
import io.github.kabirnayeem99.zakira.presentation.ui.home.HomeScreen
import io.github.kabirnayeem99.zakira.presentation.ui.home.HomeScreenNavigation
import io.github.kabirnayeem99.zakira.presentation.ui.home.HomeScreenViewModel
import io.github.kabirnayeem99.zakira.presentation.ui.quickreview.QuickReviewScreen
import io.github.kabirnayeem99.zakira.presentation.ui.quickreview.QuickReviewScreenNavigation
import io.github.kabirnayeem99.zakira.presentation.ui.quickreview.QuickReviewViewModel
import io.github.kabirnayeem99.zakira.presentation.ui.search.SearchScreen
import io.github.kabirnayeem99.zakira.presentation.ui.search.SearchScreenNavigation
import io.github.kabirnayeem99.zakira.presentation.ui.search.SearchScreenViewModel
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var arabicPhraseSyncScheduler: ArabicPhraseSyncScheduler

    override fun onStart() {
        super.onStart()
        arabicPhraseSyncScheduler.forceImmediateSync()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ZakiraLanguageMemoryTheme {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = QuickReviewScreenNavigation,
                    ) {
                        composable<QuickReviewScreenNavigation> {
                            val viewModel = hiltViewModel<QuickReviewViewModel>()
                            QuickReviewScreen(viewModel = viewModel)
                        }
                        composable<HomeScreenNavigation> {
                            val viewModel = hiltViewModel<HomeScreenViewModel>()
                            HomeScreen(viewModel = viewModel, onNavigateToSearchScreen = {
                                navController.navigate(SearchScreenNavigation)
                            }, onNavigateToFavouriteScreen = {
                                navController.navigate(FavouriteScreenNavigation)
                            })
                        }
                        composable<SearchScreenNavigation> {
                            val viewModel = hiltViewModel<SearchScreenViewModel>()
                            SearchScreen(
                                viewModel = viewModel,
                                onBack = {
                                    navController.navigateUp()
                                },
                            )
                        }
                        composable<FavouriteScreenNavigation> {
                            val viewModel = hiltViewModel<FavouriteViewModel>()
                            FavouriteScreen(
                                viewModel = viewModel,
                                onBack = {
                                    navController.navigateUp()
                                },
                            )
                        }
                    }
                }
            }
        }

    }
}

