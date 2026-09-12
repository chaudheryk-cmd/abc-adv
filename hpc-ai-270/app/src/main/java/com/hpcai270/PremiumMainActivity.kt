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

private const val PUBLISHED_DAYS = 45
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
    val prefs = remember { c.getSharedPreferences("hpc_book", Context.MODE_PRIVATE) }
    val book = remember { hpcBookDays() }
    var completed by remember { mutableIntStateOf(prefs.getInt("completed", 0).coerceIn(0, PUBLISHED_DAYS)) }
    var selectedDay by remember { mutableIntStateOf(completed.coerceIn(0, PUBLISHED_DAYS - 1)) }
    var selectedPage by remember { mutableIntStateOf(0) }
    var open by remember { mutableStateOf(false) }
    var showIndex by remember { mutableStateOf(false) }
    var bookmarkDay by remember { mutableIntStateOf(prefs.getInt("bookmark_day", -1)) }
    var bookmarkPage by remember { mutableIntStateOf(prefs.getInt("bookmark_page", -1)) }

    fun saveBookmark(day: Int, page: Int) {
        prefs.edit().putInt("bookmark_day", day).putInt("bookmark_page", page).apply()
        bookmarkDay = day
        bookmarkPage = page
    }

    fun openAt(day: Int, page: Int) {
        if (day !in book.indices) return
        selectedDay = day
        selectedPage = page.coerceIn(0, book[day].pages.lastIndex.coerceAtLeast(0))
        open = true
        showIndex = false
    }

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
            start = {
                selectedDay = completed.coerceIn(0, PUBLISHED_DAYS - 1)
                selectedPage = 0
                open = true
            },
            openIndex = { showIndex = true },
            openBookmark = { if (bookmarkDay >= 0 && bookmarkPage >= 0) openAt(bookmarkDay, bookmarkPage) },
            hasBookmark = bookmarkDay >= 0 && bookmarkPage >= 0,
            openPage = ::openAt
        )
        else -> Reader(
            di = selectedDay,
            page = selectedPage,
            setPage = { selectedPage = it.coerceIn(0, book[selectedDay].pages.lastIndex.coerceAtLeast(0)) },
            close = { open = false },
            complete = {
                if (selectedDay < PUBLISHED_DAYS) {
                    val newCompleted = maxOf(completed, selectedDay + 1).coerceAtMost(PUBLISHED_DAYS)
                    prefs.edit().putInt("completed", newCompleted).apply()
                    completed = newCompleted
                }
                selectedDay = (selectedDay + 1).coerceAtMost(PUBLISHED_DAYS - 1)
                selectedPage = 0
                open = false
            },
            openIndex = { showIndex = true },
            bookmarked = bookmarkDay == selectedDay && bookmarkPage == selectedPage,
            toggleBookmark = {
                if (bookmarkDay == selectedDay && bookmarkPage == selectedPage) {
                    prefs.edit().remove("bookmark_day").remove("bookmark_page").apply()
                    bookmarkDay = -1
                    bookmarkPage = -1
                } else saveBookmark(selectedDay, selectedPage)
            }
        )
    }
}
