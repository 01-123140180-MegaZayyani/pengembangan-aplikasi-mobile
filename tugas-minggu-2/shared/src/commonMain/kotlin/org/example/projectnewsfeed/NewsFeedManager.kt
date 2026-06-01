package org.example.projectnewsfeed

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.example.projectnewsfeed.model.NewsItem

class NewsFeedManager {

    // StateFlow Implementation
    private val _readCount = MutableStateFlow(0)
    val readCount: StateFlow<Int> = _readCount.asStateFlow()

    private val _latestNews = MutableStateFlow<NewsDisplayState>(NewsDisplayState.Loading)
    val latestNews: StateFlow<NewsDisplayState> = _latestNews.asStateFlow()

    // Coroutines Usage — async/await paralel dengan Dispatchers eksplisit
    suspend fun fetchDetail(item: NewsItem): NewsDetail = withContext(Dispatchers.Default) {
        coroutineScope {
            val sourceA = async(Dispatchers.IO) {
                try { fetchFromSourceA(item) }
                catch (e: Exception) { "Sumber A tidak tersedia: ${e.message}" }
            }
            val sourceB = async(Dispatchers.IO) {
                try { fetchFromSourceB(item) }
                catch (e: Exception) { "Sumber B tidak tersedia: ${e.message}" }
            }
            NewsDetail(summary = sourceA.await(), editorNote = sourceB.await())
        }
    }

    // Penggunaan Operators dan Implementasi Flow
    fun startFeed(scope: CoroutineScope, category: String) {
        scope.launch(Dispatchers.Default) {
            newsFeedFlow()
                .filter { it.category == category }
                .onEach { _readCount.value++ }
                .map<NewsItem, NewsDisplayState> { item ->
                    val detail = fetchDetail(item)
                    NewsDisplayState.Success(
                        item = item,
                        detail = detail,
                        readIndex = _readCount.value
                    )
                }
                .catch { e ->
                    emit(NewsDisplayState.Error("⚠️ Error: ${e.message}"))
                }
                .collect { state ->
                    _latestNews.value = state
                }
        }
    }
}

// Data class untuk structured display state
sealed class NewsDisplayState {
    object Loading : NewsDisplayState()
    data class Success(
        val item: NewsItem,
        val detail: NewsDetail,
        val readIndex: Int
    ) : NewsDisplayState()
    data class Error(val message: String) : NewsDisplayState()
}

data class NewsDetail(val summary: String, val editorNote: String)