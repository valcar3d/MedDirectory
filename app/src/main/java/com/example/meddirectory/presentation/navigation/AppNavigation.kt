package com.example.meddirectory.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import coil3.ImageLoader
import com.example.meddirectory.domain.model.FeedItem
import com.example.meddirectory.presentation.common.SalaryStats
import com.example.meddirectory.presentation.screens.detail.DetailScreen
import com.example.meddirectory.presentation.screens.feed.FeedScreen
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
object FeedRoute

@Serializable
data class DetailRoute(
    val feedItemJson: String,
    val salaryStatsJson: String?
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
                onItemClick = { item, salaryStats ->
                    val itemJson = Json.encodeToString(item)
                    val salaryStatsJson = salaryStats?.let { Json.encodeToString(it) }
                    val route = DetailRoute(itemJson, salaryStatsJson)
                    navController.navigate(route)
                }
            )
        }

        composable<DetailRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<DetailRoute>()
            val item = Json.decodeFromString<FeedItem>(route.feedItemJson)
            val salaryStats = route.salaryStatsJson?.let { Json.decodeFromString<SalaryStats>(it) }
            DetailScreen(
                item = item,
                imageLoader = imageLoader,
                salaryStats = salaryStats,
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }
    }
}