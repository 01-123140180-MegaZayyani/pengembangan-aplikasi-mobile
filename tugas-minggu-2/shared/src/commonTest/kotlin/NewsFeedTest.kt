import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.*
import org.example.projectnewsfeed.NewsFeedManager
import org.example.projectnewsfeed.newsFeedFlow
import org.example.projectnewsfeed.model.NewsItem
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class NewsFeedTest {

    // Test 1: Flow mengemit item dengan benar
    @Test
    fun testFlowEmitsItems() = runTest {
        val result = newsFeedFlow()
            .take(3)             // ambil 3 item pertama
            .toList()

        assertEquals(3, result.size)
        assertNotNull(result.first())
    }

    // Test 2: Filter kategori bekerja dengan benar
    @Test
    fun testFilterByCategory() = runTest {
        val result = newsFeedFlow()
            .take(6)
            .toList()
            .filter { it.category == "teknologi" }

        assertTrue(result.isNotEmpty())
        assertTrue(result.all { it.category == "teknologi" })
    }

    // Test 3: StateFlow readCount increment
    @Test
    fun testReadCountIncrement() = runTest {
        val manager = NewsFeedManager()

        assertEquals(0, manager.readCount.value)  // awal = 0

        manager.startFeed(this, "teknologi")
        advanceTimeBy(5000)                       // maju 5 detik

        assertTrue(manager.readCount.value > 0)
    }

    // Test 4: fetchDetail berjalan paralel (≤ 800ms, bukan 1200ms)
    @Test
    fun testFetchDetailIsParallel() = runTest {
        val manager = NewsFeedManager()
        val item = NewsItem(1, "Test", "teknologi", "TestSource")

        val start = currentTime
        manager.fetchDetail(item)
        val elapsed = currentTime - start

        assertTrue(elapsed < 800, "Harus paralel, selesai dalam <800ms")
    }
}