package usth.ict.group20.imdb

import android.os.Handler
import android.os.Looper
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import usth.ict.group20.imdb.adapters.CarouselAdapter
import usth.ict.group20.imdb.adapters.CarouselItem
import usth.ict.group20.imdb.adapters.CelebrityAdapter
import usth.ict.group20.imdb.models.Celebrity
import usth.ict.group20.imdb.adapters.FilmAdapter
import usth.ict.group20.imdb.models.Film
import usth.ict.group20.imdb.adapters.HotSearchAdapter
import usth.ict.group20.imdb.adapters.NewsArticleAdapter
import usth.ict.group20.imdb.models.NewsArticle

class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private val scrollHandler = Handler(Looper.getMainLooper())
    private lateinit var scrollRunnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // enableEdgeToEdge() // Often causes layout issues with complex layouts, can be disabled.
        setContentView(R.layout.activity_main)

        // Setup all UI components
        setupFeaturedCarousel()
        setupSearch()
        setupHotSearch()
        setupFeaturedToday() // This was previously empty
        setupWhatToWatchLists()
        setupBornToday()
        setupExploreMoviesLists()
        setupMoreToExplore()
    }

    private fun setupFeaturedCarousel() {
        // The viewPager variable is now at the class level
        viewPager = findViewById(R.id.featured_carousel_viewpager)
        // Create dummy data. Replace with your actual drawable resources.
        val items = listOf(
            CarouselItem(R.drawable.beauty),
            CarouselItem(R.drawable.redrain),
            CarouselItem(R.drawable.captain)
        )
        viewPager.adapter = CarouselAdapter(items)

        // This is the code that makes the carousel auto-scroll
        scrollRunnable = Runnable {
            var currentItem = viewPager.currentItem
            // If it's the last item, go back to the first, otherwise go to the next
            currentItem = if (currentItem == items.size - 1) 0 else currentItem + 1
            viewPager.setCurrentItem(currentItem, true) // Use true for smooth scroll
        }

        // Start the auto-scrolling
        startAutoScroll()
    }

    private fun startAutoScroll() {
        // Schedule the runnable to run every 3 seconds (3000 milliseconds)
        scrollHandler.postDelayed(scrollRunnable, 3000)

        // Also, reset the timer whenever the user manually swipes
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                scrollHandler.removeCallbacks(scrollRunnable) // Remove the old callback
                scrollHandler.postDelayed(scrollRunnable, 3000) // Schedule a new one
            }
        })
    }

    override fun onPause() {
        super.onPause()
        // Stop auto-scrolling when the app is not visible to save resources
        scrollHandler.removeCallbacks(scrollRunnable)
    }

    override fun onResume() {
        super.onResume()
        // Resume auto-scrolling when the app is visible again
        scrollHandler.postDelayed(scrollRunnable, 3000)
    }

    private fun setupSearch() {
        val searchView: SearchView = findViewById(R.id.home_search_view)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrBlank()) {
                    Toast.makeText(this@MainActivity, "Searching for: $query", Toast.LENGTH_SHORT).show()
                    searchView.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun setupHorizontalRecyclerView(recyclerViewId: Int, adapter: RecyclerView.Adapter<*>) {
        val recyclerView: RecyclerView = findViewById(recyclerViewId)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adapter
    }

    private fun setupVerticalRecyclerView(recyclerViewId: Int, adapter: RecyclerView.Adapter<*>) {
        val recyclerView: RecyclerView = findViewById(recyclerViewId)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter
    }

    private fun setupHotSearch() {
        val hotSearches = listOf("The Marvels", "Oppenheimer", "Barbie", "Trending Actors")
        setupHorizontalRecyclerView(R.id.hot_search_recyclerview, HotSearchAdapter(hotSearches))
    }

    private fun setupFeaturedToday() {
        // This section will now be populated with news articles.
        val featuredNews = listOf(
            NewsArticle(10, "Box Office Buzz: 'New Movie' Dominates", "BoxOfficeMojo", "1h ago", "The latest blockbuster has smashed opening weekend records.", "https://image.tmdb.org/t/p/w500/8YFL5QQVPy3AgrEQxNYVSgiPEbe.jpg"),
            NewsArticle(11, "Critically Acclaimed 'Indie Gem' Gets Wide Release", "IndieWire", "3h ago", "After a successful festival run, the film is now available nationwide.", "https://image.tmdb.org/t/p/w500/q719jXXEzOoYaps6babgKnONONX.jpg")
        )
        // Reusing the NewsArticleAdapter for a vertical list
        setupVerticalRecyclerView(R.id.featured_today_recyclerview, NewsArticleAdapter(featuredNews))
    }

    private fun setupWhatToWatchLists() {
        val top10List = listOf(
            Film(1, "The Shawshank Redemption", "https://image.tmdb.org/t/p/w500/q6y0Go1tsGEsmtFryDOJo3dEmqu.jpg", 9.3, 1994, "R"),
            Film(2, "The Godfather", "https://image.tmdb.org/t/p/w500/3bhkrj58Vtu7enYsRolD1fZdja1.jpg", 9.2, 1972, "R"),
            Film(3, "The Dark Knight", "https://image.tmdb.org/t/p/w500/qJ2tW6WMUDux911r6m7haRef0WH.jpg", 9.0, 2008, "PG-13"),
            Film(4, "Pulp Fiction", "https://image.tmdb.org/t/p/w500/d5iIlFn5s0ImszYzBPb8JPIfbXD.jpg", 8.9, 1994, "R")
        )

        setupHorizontalRecyclerView(R.id.top_10_recyclerview, FilmAdapter(top10List))

        val topPicksList = listOf(
            Film(10, "Interstellar", "https://image.tmdb.org/t/p/w500/rAiYTfKGqDCRIIqo664sY9XZIvQ.jpg", 8.7, 2014, "PG-13"),
            Film(11, "The Matrix", "https://image.tmdb.org/t/p/w500/f89U3ADr1oiB1s9GkdPOEpXUk5H.jpg", 8.7, 1999, "R"),
            Film(12, "Goodfellas", "https://image.tmdb.org/t/p/w500/aKuFiU82s5ISJpGZp7YkIr3kCUd.jpg", 8.7, 1990, "R")
        )
        setupHorizontalRecyclerView(R.id.top_picks_recyclerview, FilmAdapter(topPicksList))

        val inTheatersList = listOf(
            Film(20, "Dune: Part Two", "https://image.tmdb.org/t/p/w500/8b8R8l88Qje9dn9OE8PY05Nxl1X.jpg", 8.8, 2024, "PG-13"),
            Film(21, "Kung Fu Panda 4", "https://image.tmdb.org/t/p/w500/kDp1vUBnMpe8ak4rjgl3cLELqjU.jpg", 6.8, 2024, "PG")
        )
        setupHorizontalRecyclerView(R.id.in_theaters_recyclerview, FilmAdapter(inTheatersList))

        val fanFavoritesList = listOf(
            Film(30, "Back to the Future", "https://image.tmdb.org/t/p/w500/fNOH9f1aA7XRTzl1sAOx9iF553Q.jpg", 8.5, 1985, "PG"),
            Film(31, "Jurassic Park", "https://image.tmdb.org/t/p/w500/9i3plLl89DHMz7mahksDaAo7HIS.jpg", 8.2, 1993, "PG-13")
        )
        setupHorizontalRecyclerView(R.id.fans_favorites_recyclerview, FilmAdapter(fanFavoritesList))

        val oscarWinnersList = listOf(
            Film(40, "Oppenheimer", "https://upload.wikimedia.org/wikipedia/donate/5/5b/Oppenheimer_poster.jpg", 8.6, 2023, "R"),
            Film(41, "Parasite", "https://image.tmdb.org/t/p/w500/7IiTTgloJzvGI1TAYymCfbfl3vT.jpg", 8.5, 2019, "R")
        )
        setupHorizontalRecyclerView(R.id.oscar_winners_recyclerview, FilmAdapter(oscarWinnersList))
    }


    private fun setupBornToday() {
        val bornTodayList = listOf(
            Celebrity("Tom Hanks", 69, imageResId = R.drawable.tom),
            Celebrity("Scarlett Johansson", 40, imageResId = R.drawable.scarlett),
            Celebrity("Chris Evans", 43, imageResId = R.drawable.chrisevans),
            Celebrity("Zendaya", 28, imageResId = R.drawable.zendayaa),
            Celebrity("Dwayne Johnson", 53, imageResId = R.drawable.johnson)
            )
        setupHorizontalRecyclerView(R.id.born_today_recyclerview, CelebrityAdapter(bornTodayList))
    }

    private fun setupExploreMoviesLists() {
        val streamingNowList = listOf(
            Film(50, "Glass Onion", "https://image.tmdb.org/t/p/w500/vDGr1YdrlfbU9wxTOdpf3zChmv9.jpg", 7.1, 2022, "PG-13")
        )
        setupHorizontalRecyclerView(R.id.streaming_now_recyclerview, FilmAdapter(streamingNowList))

        val topBoxOfficeList = listOf(
            Film(60, "Avatar: The Way of Water", "https://image.tmdb.org/t/p/w500/t6HIqrRAclMCA60NsSmeqe9RmNV.jpg", 7.6, 2022, "PG-13")
        )
        setupHorizontalRecyclerView(R.id.top_box_office_recyclerview, FilmAdapter(topBoxOfficeList))

        val comingSoonList = emptyList<Film>()
        setupHorizontalRecyclerView(R.id.coming_soon_recyclerview, FilmAdapter(comingSoonList))
    }

    private fun setupMoreToExplore() {
        /*val popularInterestsList = emptyList<Film>()
        setupHorizontalRecyclerView(R.id.popular_interests_recyclerview, FilmAdapter(popularInterestsList))

        val newsList = listOf(
            NewsArticle(1, "Major Movie News Headline", "Variety", "2h ago", "This is a short summary...", "url_placeholder"),
            NewsArticle(2, "Another Big Casting Announcement", "Deadline", "5h ago", "Details about the upcoming blockbuster...", "url_placeholder"),
            NewsArticle(3, "Festival Film Sells for Record Price", "THR", "1d ago", "The indie darling is heading to a major studio...", "url_placeholder")
        )
        setupVerticalRecyclerView(R.id.top_news_more_recyclerview, NewsArticleAdapter(newsList))

         */
        findViewById<ImageButton>(R.id.btn_social_facebook).setOnClickListener { openUrl("https://www.facebook.com/imdb") }
        findViewById<ImageButton>(R.id.btn_social_twitter).setOnClickListener { openUrl("https://www.twitter.com/imdb") }
        findViewById<ImageButton>(R.id.btn_social_instagram).setOnClickListener { openUrl("https://www.instagram.com/imdb") }

    }


    private fun openUrl(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }
}
