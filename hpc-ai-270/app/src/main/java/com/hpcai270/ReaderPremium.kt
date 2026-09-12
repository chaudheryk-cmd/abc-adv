package com.hpcai270

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.abs

private val Paper = Color(0xFFFFFDF4)
private val Ink = Color(0xFF202B3A)
private val BlueInk = Color(0xFF173F8A)
private val Plum = Color(0xFF6B3F8F)
private val Pink = Color(0xFFF4A6C0)
private val Yellow = Color(0xFFFFE58A)
private val Green = Color(0xFFBDE8C7)
private val Sky = Color(0xFFBBDFF7)
private val Orange = Color(0xFFFFC98A)
private val Muted = Color(0xFF716A73)
private val Rule = Color(0xFFDCE6EF)
private val Leather = Color(0xFF101A27)

@Composable
fun Reader(di: Int, page: Int, setPage: (Int) -> Unit, close: () -> Unit, complete: () -> Unit, openIndex: () -> Unit, bookmarked: Boolean, toggleBookmark: () -> Unit) {
    val all = remember { hpcBookDays().take(45) }
    val d = all.getOrNull(di)
    val pageCount = d?.pages?.size ?: 2
    val safePage = page.coerceIn(0, pageCount - 1)
    var previousPage by remember(di) { mutableIntStateOf(safePage) }
    var dragAmount by remember { mutableFloatStateOf(0f) }
    val forward = safePage >= previousPage
    LaunchedEffect(safePage) { previousPage = safePage }
    Column(Modifier.fillMaxSize().background(Leather)) {
        ReaderTopBar(di, safePage, bookmarked, close, openIndex, toggleBookmark)
        LinearProgressIndicator(progress = { ((di + 1).toFloat() / 45f).coerceIn(0f, 1f) }, modifier = Modifier.fillMaxWidth().height(3.dp), color = Color(0xFF9569D9), trackColor = Color(0xFF2D3948))
        Box(Modifier.fillMaxWidth().weight(1f).padding(horizontal = 12.dp, vertical = 10.dp).pointerInput(di, safePage) {
            detectHorizontalDragGestures(
                onDragStart = { dragAmount = 0f },
                onHorizontalDrag = { _, amount -> dragAmount += amount },
                onDragEnd = {
                    if (abs(dragAmount) > 90f) {
                        if (dragAmount < 0 && safePage < pageCount - 1) setPage(safePage + 1)
                        if (dragAmount > 0 && safePage > 0) setPage(safePage - 1)
                    }
                    dragAmount = 0f
                }
            )
        }, Alignment.Center) {
            if (d == null) Text("This chapter is being prepared.", color = Color.White, fontFamily = Handwritten, fontSize = 22.sp)
            else AnimatedContent(targetState = safePage, transitionSpec = {
                if (forward) (slideInHorizontally { it } + fadeIn()).togetherWith(slideOutHorizontally { -it / 3 } + fadeOut())
                else (slideInHorizontally { -it } + fadeIn()).togetherWith(slideOutHorizontally { it / 3 } + fadeOut())
            }, label = "physicalPageTurn") { animatedPage -> NotebookPage(d, animatedPage) }
        }
        ReaderBottomBar(safePage, pageCount, setPage, complete)
    }
}

@Composable
private fun ReaderTopBar(day: Int, page: Int, bookmarked: Boolean, close: () -> Unit, openIndex: () -> Unit, toggleBookmark: () -> Unit) {
    Row(Modifier.fillMaxWidth().height(60.dp).padding(horizontal = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        TextButton(onClick = close) { Text("‹  Close", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold) }
        Spacer(Modifier.weight(1f))
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("KUNAL'S JOURNEY", color = Color.White, fontWeight = FontWeight.Black, fontSize = 12.sp, letterSpacing = 1.2.sp)
            Text("HPC + AI  •  DAY ${day + 1}  •  ${page + 1}/2", color = Color(0xFFC8D2DF), fontSize = 11.sp)
        }
        Spacer(Modifier.weight(1f))
        TextButton(onClick = openIndex) { Text("INDEX", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp) }
        IconButton(onClick = toggleBookmark) { Text(if (bookmarked) "★" else "☆", color = if (bookmarked) Color(0xFFFFD86B) else Color.White, fontSize = 27.sp) }
    }
}

@Composable
private fun ReaderBottomBar(page: Int, pageCount: Int, setPage: (Int) -> Unit, complete: () -> Unit) {
    Row(Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 7.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        OutlinedButton(onClick = { setPage((page - 1).coerceAtLeast(0)) }, enabled = page > 0, shape = RoundedCornerShape(18.dp)) { Text("← Previous", fontFamily = Handwritten, fontSize = 16.sp) }
        Text("SWIPE  ←  →", color = Color(0xFFD2D9E2), fontFamily = Handwritten, fontSize = 17.sp)
        if (page < pageCount - 1) Button(onClick = { setPage(page + 1) }, shape = RoundedCornerShape(18.dp)) { Text("Next →", fontFamily = Handwritten, fontSize = 17.sp) }
        else Button(onClick = complete, shape = RoundedCornerShape(18.dp)) { Text("✓ Complete day", fontFamily = Handwritten, fontSize = 17.sp) }
    }
}

@Composable
private fun NotebookPage(d: Day, page: Int) {
    Card(Modifier.fillMaxWidth().fillMaxHeight(.99f).shadow(18.dp, RoundedCornerShape(8.dp)), colors = CardDefaults.cardColors(containerColor = Paper), shape = RoundedCornerShape(8.dp)) {
        Box(Modifier.fillMaxSize()) {
            RuledPaper()
            Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(start = 38.dp, end = 22.dp, top = 17.dp, bottom = 30.dp)) {
                BookPageHeader(d, page)
                Spacer(Modifier.height(8.dp))
                if (page == 0) {
                    RealHardwareReference(d.number)
                    LessonVisual(d.number)
                    Spacer(Modifier.height(12.dp))
                }
                PremiumContent(d.pages[page])
                Spacer(Modifier.height(18.dp))
                Text(if (page == 0) "↳ turn the page — the second page goes deeper" else "✎ field note: explain this topic without looking at the page", fontFamily = Handwritten, fontSize = 16.sp, color = Muted)
            }
            Canvas(Modifier.align(Alignment.BottomEnd).size(30.dp)) { drawLine(Color(0xFFC8BCA4), Offset(2f, 28f), Offset(28f, 2f), 2f) }
        }
    }
}

@Composable
private fun BookPageHeader(d: Day, page: Int) {
    Row(verticalAlignment = Alignment.Top) {
        Box(Modifier.background(listOf(Yellow, Pink, Sky, Green, Orange)[(d.number - 1).mod(5)], RoundedCornerShape(10.dp)).padding(horizontal = 11.dp, vertical = 7.dp)) { Text("DAY ${d.number}", fontFamily = Handwritten, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Ink) }
        Spacer(Modifier.width(10.dp))
        Column(Modifier.weight(1f)) {
            Text(d.title, fontFamily = Handwritten, fontWeight = FontWeight.Bold, fontSize = 28.sp, lineHeight = 29.sp, maxLines = 2, color = BlueInk)
            Text(d.topic, fontFamily = Handwritten, fontSize = 18.sp, lineHeight = 20.sp, color = Plum, maxLines = 2)
        }
        Text("${page + 1}/2", fontFamily = Handwritten, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Muted)
    }
}

@Composable
private fun RuledPaper() {
    Canvas(Modifier.fillMaxSize()) {
        var y = 55f
        while (y < size.height) { drawLine(Rule, Offset(0f, y), Offset(size.width, y), 1f); y += 28f }
        drawLine(Color(0xFFEFA6AE), Offset(25f, 0f), Offset(25f, size.height), 2f)
    }
}

@Composable
private fun PremiumContent(raw: String) {
    val lines = raw.replace("\r", "").split("\n")
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        lines.forEachIndexed { index, original ->
            val line = original.trim()
            if (line.isBlank()) return@forEachIndexed
            val bullet = line.startsWith("•") || line.startsWith("-") || line.startsWith("→")
            val heading = isHeading(line)
            when {
                heading -> {
                    val fill = listOf(Pink, Yellow, Sky, Green, Orange)[index.mod(5)]
                    Box(Modifier.background(fill, RoundedCornerShape(7.dp)).padding(horizontal = 10.dp, vertical = 4.dp)) { Text(line.removePrefix("#").trim(), fontFamily = Handwritten, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Ink) }
                }
                bullet -> Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
                    Text("✦", fontFamily = Handwritten, fontWeight = FontWeight.Bold, color = Plum, fontSize = 18.sp, modifier = Modifier.width(21.dp))
                    Text(cleanBullet(line), fontFamily = Handwritten, fontSize = 18.sp, lineHeight = 25.sp, color = BlueInk)
                }
                else -> Text(line.replace("**", ""), fontFamily = Handwritten, fontSize = 18.sp, lineHeight = 25.sp, color = BlueInk)
            }
        }
    }
}

private fun isHeading(s: String): Boolean {
    val letters = s.filter { it.isLetter() }
    val upper = letters.isNotEmpty() && letters.count { it.isUpperCase() }.toFloat() / letters.length > .72f
    val keywords = listOf("KEY IDEA", "CORE IDEA", "REAL WORLD", "WHY IT MATTERS", "HANDS-ON", "INTERVIEW", "TROUBLESHOOTING", "REMEMBER", "VOCABULARY", "ARCHITECTURE", "TOOLS", "CHECKPOINT", "THE BIG PICTURE", "NEXT STEP", "TASK", "STORAGE VIEW", "AI TRAINING", "DATA PATH", "MODEL", "WORKLOAD", "COMMANDS", "DEEP DIVE")
    return s.startsWith("#") || keywords.any { s.uppercase().startsWith(it) } || (upper && s.length < 82)
}

private fun cleanBullet(s: String) = s.removePrefix("•").removePrefix("-").removePrefix("→").trim()
