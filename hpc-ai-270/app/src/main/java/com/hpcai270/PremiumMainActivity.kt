package com.hpcai270

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat

private const val PUBLISHED_DAYS = 270
private const val PREFS = "hpc_book"
private const val KEY_COMPLETED = "completed"
private const val KEY_LAST_DAY = "last_day"
private const val KEY_LAST_PAGE = "last_page"
private const val KEY_BOOKMARK_DAY = "bookmark_day"
private const val KEY_BOOKMARK_PAGE = "bookmark_page"
private val AppChrome = Color(0xFF101A27)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        WindowCompat.getInsetsController(window, window.decorView).apply {
            isAppearanceLightStatusBars = false
            isAppearanceLightNavigationBars = false
        }
        setContent {
            MaterialTheme {
                Box(Modifier.fillMaxSize().background(AppChrome)) {
                    Box(Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing)) {
                        HpcBookPremium(this@MainActivity)
                    }
                }
            }
        }
    }
}

@Composable
private fun HpcBookPremium(c: Context) {
    val prefs = remember { c.getSharedPreferences(PREFS, Context.MODE_PRIVATE) }
    val book = remember { hpcBookDays() }

    var completed by remember { mutableIntStateOf(prefs.getInt(KEY_COMPLETED, 0).coerceIn(0, PUBLISHED_DAYS)) }
    var selectedDay by remember {
        mutableIntStateOf(
            prefs.getInt(KEY_LAST_DAY, completed.coerceAtMost(PUBLISHED_DAYS - 1))
                .coerceIn(0, PUBLISHED_DAYS - 1)
        )
    }
    var selectedPage by remember {
        mutableIntStateOf(
            prefs.getInt(KEY_LAST_PAGE, 0).coerceAtLeast(0)
        )
    }
    var open by remember { mutableStateOf(false) }
    var showIndex by remember { mutableStateOf(false) }
    var bookmarkDay by remember { mutableIntStateOf(prefs.getInt(KEY_BOOKMARK_DAY, -1)) }
    var bookmarkPage by remember { mutableIntStateOf(prefs.getInt(KEY_BOOKMARK_PAGE, -1)) }

    fun persistPosition(day: Int, page: Int) {
        if (day !in book.indices) return
        val safePage = page.coerceIn(0, book[day].pages.lastIndex.coerceAtLeast(0))
        prefs.edit().putInt(KEY_LAST_DAY, day).putInt(KEY_LAST_PAGE, safePage).apply()
        selectedDay = day
        selectedPage = safePage
    }

    fun saveBookmark(day: Int, page: Int) {
        prefs.edit().putInt(KEY_BOOKMARK_DAY, day).putInt(KEY_BOOKMARK_PAGE, page).apply()
        bookmarkDay = day
        bookmarkPage = page
    }

    fun openAt(day: Int, page: Int) {
        if (day !in book.indices) return
        persistPosition(day, page)
        open = true
        showIndex = false
    }

    val safeSelectedPage = selectedPage.coerceIn(
        0,
        book.getOrNull(selectedDay)?.pages?.lastIndex?.coerceAtLeast(0) ?: 0
    )

    when {
        showIndex -> BookIndex(
            completed = completed,
            bookmarkedDay = bookmarkDay.takeIf { it >= 0 },
            bookmarkedPage = bookmarkPage.takeIf { it >= 0 },
            openPage = ::openAt,
            close = { showIndex = false }
        )

        !open -> Cover(
            day = completed,
            currentDay = selectedDay,
            currentPage = safeSelectedPage,
            start = {
                persistPosition(selectedDay, safeSelectedPage)
                open = true
            },
            openIndex = { showIndex = true },
            openBookmark = {
                if (bookmarkDay >= 0 && bookmarkPage >= 0) openAt(bookmarkDay, bookmarkPage)
            },
            hasBookmark = bookmarkDay >= 0 && bookmarkPage >= 0,
            openPage = ::openAt
        )

        else -> Reader(
            di = selectedDay,
            page = safeSelectedPage,
            setPage = { persistPosition(selectedDay, it) },
            close = {
                persistPosition(selectedDay, safeSelectedPage)
                open = false
            },
            complete = {
                val dayCompleted = selectedDay
                val newCompleted = maxOf(completed, dayCompleted + 1).coerceAtMost(PUBLISHED_DAYS)
                prefs.edit().putInt(KEY_COMPLETED, newCompleted).apply()
                completed = newCompleted

                if (dayCompleted < PUBLISHED_DAYS - 1) {
                    // Completion advances directly into the next day instead of
                    // throwing the learner back to the cover screen.
                    persistPosition(dayCompleted + 1, 0)
                    open = true
                } else {
                    // Day 270 is the end of the curriculum. Keep the final
                    // position saved and return to the dashboard only here.
                    persistPosition(PUBLISHED_DAYS - 1, 0)
                    open = false
                }
            },
            openIndex = { showIndex = true },
            bookmarked = bookmarkDay == selectedDay && bookmarkPage == safeSelectedPage,
            toggleBookmark = {
                if (bookmarkDay == selectedDay && bookmarkPage == safeSelectedPage) {
                    prefs.edit().remove(KEY_BOOKMARK_DAY).remove(KEY_BOOKMARK_PAGE).apply()
                    bookmarkDay = -1
                    bookmarkPage = -1
                } else {
                    saveBookmark(selectedDay, safeSelectedPage)
                }
            }
        )
    }
}
