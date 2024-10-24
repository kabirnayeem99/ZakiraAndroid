package io.github.kabirnayeem99.zakira.presentation

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
import io.github.kabirnayeem99.zakira.presentation.common.config.ZakiraLanguageMemoryTheme
import io.github.kabirnayeem99.zakira.presentation.favourite.FavouriteScreen
import io.github.kabirnayeem99.zakira.presentation.favourite.FavouriteScreenNavigation
import io.github.kabirnayeem99.zakira.presentation.favourite.FavouriteViewModel
import io.github.kabirnayeem99.zakira.presentation.home.HomeScreen
import io.github.kabirnayeem99.zakira.presentation.home.HomeScreenNavigation
import io.github.kabirnayeem99.zakira.presentation.home.HomeScreenViewModel
import io.github.kabirnayeem99.zakira.presentation.search.SearchScreen
import io.github.kabirnayeem99.zakira.presentation.search.SearchScreenNavigation
import io.github.kabirnayeem99.zakira.presentation.search.SearchScreenViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ZakiraLanguageMemoryTheme {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController, startDestination = HomeScreenNavigation
                    ) {
                        composable<HomeScreenNavigation> {
                            val viewModel = hiltViewModel<HomeScreenViewModel>()
                            HomeScreen(
                                viewModel = viewModel,
                                onNavigateToSearchScreen = {
                                    navController.navigate(SearchScreenNavigation)
                                },
                                onNavigateToFavouriteScreen = {
                                    navController.navigate(FavouriteScreenNavigation)
                                }
                            )
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

