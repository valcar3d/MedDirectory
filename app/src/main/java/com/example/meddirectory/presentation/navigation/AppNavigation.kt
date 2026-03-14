package com.example.meddirectory.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil3.ImageLoader
import com.example.meddirectory.presentation.screens.detail.DetailScreen
import com.example.meddirectory.presentation.screens.detail.DetailViewModel
import com.example.meddirectory.presentation.screens.feed.FeedScreen
import kotlinx.serialization.Serializable

@Serializable
object FeedRoute

@Serializable
data class DetailRoute(
    val itemId: String
)

@Composable
fun AppNavigation(
    imageLoader: ImageLoader,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = FeedRoute
    ) {
        composable<FeedRoute> {
            FeedScreen(
                imageLoader = imageLoader,
                onItemClick = { itemId ->
                    navController.navigate(DetailRoute(itemId))
                }
            )
        }

        composable<DetailRoute> { _ ->
            val viewModel: DetailViewModel = hiltViewModel()
            DetailScreen(
                viewModel = viewModel,
                imageLoader = imageLoader,
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }
    }
}