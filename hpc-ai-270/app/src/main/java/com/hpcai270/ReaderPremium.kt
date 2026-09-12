package com.hpcai270

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import kotlin.math.abs

private val Paper = Color(0xFFFFFDF5)
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
    val all = remember { hpcBookDays() }
    val d = all.getOrNull(di)
    val pageCount = d?.pages?.size ?: 1
    val rotation = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()
    var dragDistance by remember { mutableFloatStateOf(0f) }
    var turning by remember { mutableStateOf(false) }

    Column(Modifier.fillMaxSize().background(Leather)) {
        ReaderTopBar(di, page.coerceIn(0, (pageCount - 1).coerceAtLeast(0)), pageCount, bookmarked, close, openIndex, toggleBookmark)
        LinearProgressIndicator(
            progress = { ((di + page.coerceIn(0, (pageCount - 1).coerceAtLeast(0)).toFloat() / pageCount.coerceAtLeast(1)) / 45f).coerceIn(0f, 1f) },
            modifier = Modifier.fillMaxWidth().height(3.dp),
            color = Color(0xFF9569D9),
            trackColor = Color(0xFF2D3948)
        )
        BoxWithConstraints(
            Modifier.fillMaxWidth().weight(1f).padding(horizontal = 12.dp, vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            val widthPx = constraints.maxWidth.toFloat().coerceAtLeast(1f)
            val safePage = page.coerceIn(0, (pageCount - 1).coerceAtLeast(0))
            val canGoBack = safePage > 0
            val canGoForward = safePage < pageCount - 1
            val direction = if (dragDistance < 0f) -1f else 1f
            val targetPage = when {
                dragDistance < -20f && canGoForward -> safePage + 1
                dragDistance > 20f && canGoBack -> safePage - 1
                else -> safePage
            }

            if (d == null) {
                Text("This chapter is being prepared.", color = Color.White, fontFamily = Handwritten, fontSize = 22.sp)
            } else {
                // The destination sheet sits underneath the turning sheet, just like a real book.
                if (targetPage != safePage) {
                    NotebookPage(
                        d = d,
                        page = targetPage,
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer {
                                val reveal = (abs(dragDistance) / widthPx).coerceIn(0f, 1f)
                                scaleX = .985f + reveal * .015f
                                scaleY = .985f + reveal * .015f
                                alpha = .82f + reveal * .18f
                            }
                    )
                }

                // A physical-looking sheet: follows the finger, rotates around its actual binding edge,
                // casts a moving shadow and darkens toward the fold as it turns.
                Box(
                    Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            rotationY = rotation.value
                            transformOrigin = if (rotation.value < 0f) TransformOrigin(0f, .5f) else TransformOrigin(1f, .5f)
                            cameraDistance = 48f * density
                            shadowElevation = if (abs(rotation.value) > 1f) 22f else 8f
                            scaleX = 1f - (abs(rotation.value) / 92f) * .018f
                        }
                        .pointerInput(di, safePage, pageCount, widthPx) {
                            detectHorizontalDragGestures(
                                onDragStart = {
                                    dragDistance = 0f
                                    turning = true
                                },
                                onHorizontalDrag = { _, amount ->
                                    dragDistance += amount
                                    val allowed = (dragDistance < 0f && canGoForward) || (dragDistance > 0f && canGoBack)
                                    if (allowed) {
                                        // One physical page turn is ~90 degrees. Keep a little resistance near the edge.
                                        val normalized = (dragDistance / widthPx).coerceIn(-1f, 1f)
                                        val degrees = (-normalized * 92f).coerceIn(-92f, 92f)
                                        scope.launch { rotation.snapTo(degrees) }
                                    } else {
                                        scope.launch { rotation.snapTo(0f) }
                                    }
                                },
                                onDragEnd = {
                                    val forward = dragDistance < -widthPx * .22f && canGoForward
                                    val backward = dragDistance > widthPx * .22f && canGoBack
                                    dragDistance = 0f
                                    turning = false
                                    scope.launch {
                                        when {
                                            forward -> {
                                                rotation.animateTo(-92f, tween(330, easing = FastOutSlowInEasing))
                                                setPage(safePage + 1)
                                                rotation.snapTo(0f)
                                            }
                                            backward -> {
                                                rotation.animateTo(92f, tween(330, easing = FastOutSlowInEasing))
                                                setPage(safePage - 1)
                                                rotation.snapTo(0f)
                                            }
                                            else -> rotation.animateTo(0f, tween(300, easing = FastOutSlowInEasing))
                                        }
                                    }
                                }
                            )
                        }
                ) {
                    NotebookPage(d = d, page = safePage, modifier = Modifier.fillMaxSize())

                    // Moving fold/shadow. It makes the sheet read as a physical object rather than a flat 3D card.
                    if (turning || abs(rotation.value) > .5f) {
                        val fold = (abs(rotation.value) / 92f).coerceIn(0f, 1f)
                        Box(
                            Modifier
                                .fillMaxHeight()
                                .width(90.dp)
                                .align(if (rotation.value < 0f) Alignment.CenterStart else Alignment.CenterEnd)
                                .background(
                                    Brush.horizontalGradient(
                                        if (rotation.value < 0f) listOf(Color.Black.copy(alpha = .28f * fold), Color.Transparent)
                                        else listOf(Color.Transparent, Color.Black.copy(alpha = .28f * fold))
                                    )
                                )
                        )
                        Box(
                            Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(alpha = .025f + fold * .07f))
                        )
                    }
                }
            }
        }
        ReaderBottomBar(page.coerceIn(0, (pageCount - 1).coerceAtLeast(0)), pageCount, setPage, complete)
    }
}

@Composable
private fun ReaderTopBar(day: Int, page: Int, pageCount: Int, bookmarked: Boolean, close: () -> Unit, openIndex: () -> Unit, toggleBookmark: () -> Unit) {
    Row(Modifier.fillMaxWidth().height(60.dp).padding(horizontal = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        TextButton(onClick = close) { Text("‹  Close", color = Color.White, fontFamily = Handwritten, fontSize = 16.sp, fontWeight = FontWeight.SemiBold) }
        Spacer(Modifier.weight(1f))
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("KUNAL'S JOURNEY", color = Color.White, fontFamily = Handwritten, fontWeight = FontWeight.Black, fontSize = 12.sp, letterSpacing = 1.2.sp)
            Text("HPC + AI  •  DAY ${day + 1}  •  ${page + 1}/$pageCount", color = Color(0xFFC8D2DF), fontFamily = Handwritten, fontSize = 11.sp)
        }
        Spacer(Modifier.weight(1f))
        TextButton(onClick = openIndex) { Text("INDEX", color = Color.White, fontFamily = Handwritten, fontWeight = FontWeight.Bold, fontSize = 11.sp) }
        IconButton(onClick = toggleBookmark) { Text(if (bookmarked) "★" else "☆", fontFamily = Handwritten, color = if (bookmarked) Color(0xFFFFD86B) else Color.White, fontSize = 27.sp) }
    }
}

@Composable
private fun ReaderBottomBar(page: Int, pageCount: Int, setPage: (Int) -> Unit, complete: () -> Unit) {
    Row(Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 7.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        OutlinedButton(onClick = { setPage((page - 1).coerceAtLeast(0)) }, enabled = page > 0, shape = RoundedCornerShape(18.dp)) { Text("← Previous", fontFamily = Handwritten, fontSize = 16.sp) }
        Text("PAGE ${page + 1} / $pageCount  •  SWIPE", color = Color(0xFFD2D9E2), fontFamily = Handwritten, fontSize = 16.sp)
        if (page < pageCount - 1) Button(onClick = { setPage(page + 1) }, shape = RoundedCornerShape(18.dp)) { Text("Next →", fontFamily = Handwritten, fontSize = 17.sp) }
        else Button(onClick = complete, shape = RoundedCornerShape(18.dp)) { Text("✓ Complete day", fontFamily = Handwritten, fontSize = 17.sp) }
    }
}

@Composable
private fun NotebookPage(d: Day, page: Int, modifier: Modifier = Modifier) {
    Card(modifier.shadow(18.dp, RoundedCornerShape(8.dp)), colors = CardDefaults.cardColors(containerColor = Paper), shape = RoundedCornerShape(8.dp)) {
        Box(Modifier.fillMaxSize()) {
            RuledPaper()
            Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(start = 38.dp, end = 22.dp, top = 17.dp, bottom = 30.dp)) {
                BookPageHeader(d, page, d.pages.size)
                Spacer(Modifier.height(8.dp))
                if (page == 0) {
                    RealHardwareReference(d.number)
                    LessonVisual(d.number)
                    Spacer(Modifier.height(12.dp))
                }
                PremiumContent(d.pages[page])
                Spacer(Modifier.height(18.dp))
                Text(
                    if (page == d.pages.lastIndex) "✎ END OF DAY ${d.number} — explain the topic from memory before moving on"
                    else "↳ keep reading — this lesson is intentionally deeper than a fixed page count",
                    fontFamily = Handwritten,
                    fontSize = 16.sp,
                    color = Muted
                )
            }
            Canvas(Modifier.align(Alignment.BottomEnd).size(30.dp)) { drawLine(Color(0xFFC8BCA4), Offset(2f, 28f), Offset(28f, 2f), 2f) }
        }
    }
}

@Composable
private fun BookPageHeader(d: Day, page: Int, pageCount: Int) {
    Row(verticalAlignment = Alignment.Top) {
        Box(Modifier.background(listOf(Yellow, Pink, Sky, Green, Orange)[(d.number - 1).mod(5)], RoundedCornerShape(10.dp)).padding(horizontal = 11.dp, vertical = 7.dp)) {
            Text("DAY ${d.number}", fontFamily = Handwritten, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Ink)
        }
        Spacer(Modifier.width(10.dp))
        Column(Modifier.weight(1f)) {
            Text(d.title, fontFamily = Handwritten, fontWeight = FontWeight.Bold, fontSize = 28.sp, lineHeight = 29.sp, maxLines = 3, color = BlueInk)
            Text(d.topic, fontFamily = Handwritten, fontSize = 18.sp, lineHeight = 20.sp, color = Plum, maxLines = 3)
        }
        Text("${page + 1}/$pageCount", fontFamily = Handwritten, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Muted)
    }
}

@Composable
private fun RuledPaper() {
    Canvas(Modifier.fillMaxSize()) {
        var y = 55f
        while (y < size.height) {
            drawLine(Rule, Offset(0f, y), Offset(size.width, y), 1f)
            y += 28f
        }
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
                    Box(Modifier.background(fill, RoundedCornerShape(7.dp)).padding(horizontal = 10.dp, vertical = 4.dp)) {
                        Text(line.removePrefix("#").trim(), fontFamily = Handwritten, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Ink)
                    }
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
