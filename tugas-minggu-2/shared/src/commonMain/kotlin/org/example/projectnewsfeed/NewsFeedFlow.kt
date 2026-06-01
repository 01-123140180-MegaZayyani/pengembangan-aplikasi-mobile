package org.example.projectnewsfeed
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.example.projectnewsfeed.model.NewsItem
import org.example.projectnewsfeed.model.sampleNews

// Flow builder yang emit berita baru setiap 2 detik
fun newsFeedFlow(): Flow<NewsItem> = flow {
    var index = 0
    while (true) {
        val item = sampleNews[index % sampleNews.size]
        emit(item)                    // emit satu berita
        index++
        delay(2000)                   // tunggu 2 detik sebelum berita berikutnya
    }
}

// Suspend function untuk ambil detail dari satu sumber
suspend fun fetchFromSourceA(item: NewsItem): String {
    delay(500)                       // simulasi network call
    return "[${item.source}] ${item.title} — ringkasan dari sumber A"
}

suspend fun fetchFromSourceB(item: NewsItem): String {
    delay(700)                       // simulasi network call lebih lambat
    return "Opini editor: artikel ini relevan untuk kategori ${item.category}"
}