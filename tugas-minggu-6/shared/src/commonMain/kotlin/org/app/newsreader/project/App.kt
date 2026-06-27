package org.app.newsreader.project

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.app.newsreader.project.data.HttpClientFactory
import org.app.newsreader.project.data.NewsRepository
import org.app.newsreader.project.navigation.Routes
import org.app.newsreader.project.ui.NewsDetailScreen
import org.app.newsreader.project.ui.NewsDetailViewModel
import org.app.newsreader.project.ui.NewsListScreen
import org.app.newsreader.project.ui.NewsListViewModel

/**
 * Root composable aplikasi "News Reader".
 * Membuat HttpClient + Repository sekali saja (di-remember), lalu menyediakannya
 * ke setiap screen lewat NavHost. Ini contoh sederhana dependency wiring tanpa DI library.
 */
@Composable
fun App() {
    MaterialTheme {
        Surface(modifier = Modifier) {
            val repository = remember { NewsRepository(HttpClientFactory.create()) }
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = Routes.LIST) {
                composable(Routes.LIST) {
                    val viewModel = viewModel { NewsListViewModel(repository) }
                    NewsListScreen(viewModel = viewModel) { id ->
                        navController.navigate(Routes.detail(id))
                    }
                }

                composable(
                    route = Routes.DETAIL,
                    arguments = listOf(navArgument("articleId") { type = NavType.IntType })
                ) { backStackEntry ->
                    val articleId = backStackEntry.arguments?.getInt("articleId") ?: 0
                    val viewModel = viewModel { NewsDetailViewModel(repository, articleId) }
                    NewsDetailScreen(
                        viewModel = viewModel,
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}
