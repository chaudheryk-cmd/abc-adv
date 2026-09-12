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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
private val Leather = Color(0xFF111B27)

@Composable
fun Reader(
    di: Int,
    page: Int,
    setPage: (Int) -> Unit,
    close: () -> Unit,
    complete: () -> Unit,
    openIndex: () -> Unit,
    bookmarked: Boolean,
    toggleBookmark: () -> Unit
) {
    val all = remember { hpcBookDays().take(45) }
    val d = all.getOrNull(di)
    val pageCount = d?.pages?.size ?: 2
    val safePage = page.coerceIn(0, pageCount - 1)
    var previousPage by remember(di) { mutableIntStateOf(safePage) }
    var dragAmount by remember { mutableFloatStateOf(0f) }
    val forward = safePage >= previousPage
    LaunchedEffect(safePage) { previousPage = safePage }

    Column(Modifier.fillMaxSize().background(Leather)) {
        // A restrained book toolbar — the page itself is the hero, not a generic app bar.
        Row(
            Modifier.fillMaxWidth().height(56.dp).padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = close) {
                Text("‹  Close", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
            }
            Spacer(Modifier.weight(1f))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("KUNAL'S JOURNEY", color = Color.White, fontWeight = FontWeight.Black, fontSize = 12.sp, letterSpacing = 1.sp)
                Text("HPC + AI  •  DAY ${di + 1}  •  ${safePage + 1}/2", color = Color(0xFFC8D2DF), fontSize = 10.sp)
            }
            Spacer(Modifier.weight(1f))
            TextButton(onClick = openIndex) { Text("INDEX", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp) }
            IconButton(onClick = toggleBookmark) {
                Text(if (bookmarked) "★" else "☆", color = if (bookmarked) Color(0xFFFFD86B) else Color.White, fontSize = 24.sp)
            }
        }

        LinearProgressIndicator(
            progress = { ((di + 1).toFloat() / 45f).coerceIn(0f, 1f) },
            modifier = Modifier.fillMaxWidth().height(3.dp),
            color = Color(0xFF8E63D5),
            trackColor = Color(0xFF2D3A48)
        )

        Box(
            Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 10.dp, vertical = 9.dp)
                .pointerInput(di, safePage) {
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
                },
            Alignment.Center
        ) {
            if (d == null) {
                Text("This chapter is being prepared.", color = Color.White, fontFamily = FontFamily.Cursive, fontSize = 22.sp)
            } else {
                AnimatedContent(
                    targetState = safePage,
                    transitionSpec = {
                        if (forward) {
                            (slideInHorizontally { it } + fadeIn()).togetherWith(slideOutHorizontally { -it / 3 } + fadeOut())
                        } else {
                            (slideInHorizontally { -it } + fadeIn()).togetherWith(slideOutHorizontally { it / 3 } + fadeOut())
                        }
                    },
                    label = "bookPageTurn"
                ) { animatedPage ->
                    NotebookPage(d, animatedPage)
                }
            }
        }

        Row(
            Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = { setPage((safePage - 1).coerceAtLeast(0)) },
                enabled = safePage > 0,
                shape = RoundedCornerShape(18.dp)
            ) { Text("← Previous") }

            Text("SWIPE  ←  →", color = Color(0xFFC8D2DF), fontFamily = FontFamily.Cursive, fontSize = 14.sp)

            if (safePage < pageCount - 1) {
                Button(onClick = { setPage(safePage + 1) }, shape = RoundedCornerShape(18.dp)) { Text("Next →") }
            } else {
                Button(onClick = complete, shape = RoundedCornerShape(18.dp)) { Text("✓ Complete day") }
            }
        }
    }
}

@Composable
private fun NotebookPage(d: Day, page: Int) {
    Card(
        Modifier.fillMaxWidth().fillMaxHeight(.99f).shadow(22.dp, RoundedCornerShape(7.dp)),
        colors = CardDefaults.cardColors(containerColor = Paper),
        shape = RoundedCornerShape(7.dp)
    ) {
        Box(Modifier.fillMaxSize()) {
            RuledPaper()
            Column(
                Modifier.fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(start = 36.dp, end = 22.dp, top = 18.dp, bottom = 24.dp)
            ) {
                BookPageHeader(d, page)
                Spacer(Modifier.height(8.dp))

                if (page == 0) {
                    ConceptSketch(d.number)
                    Spacer(Modifier.height(10.dp))
                }

                PremiumContent(d.pages[page], compact = false)

                if (page == 1) {
                    Spacer(Modifier.height(16.dp))
                    CheckpointCard(d.task)
                }
            }

            // A subtle page curl marker makes the page feel physical without taking space from the notes.
            Canvas(Modifier.align(Alignment.BottomEnd).size(28.dp)) {
                drawLine(Color(0xFFD1C6AE), androidx.compose.ui.geometry.Offset(2f, 26f), androidx.compose.ui.geometry.Offset(26f, 2f), 2f)
            }
        }
    }
}

@Composable
private fun BookPageHeader(d: Day, page: Int) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            Modifier.background(
                listOf(Yellow, Pink, Sky, Green, Orange)[(d.number - 1).mod(5)],
                RoundedCornerShape(9.dp)
            ).padding(horizontal = 12.dp, vertical = 7.dp)
        ) {
            Text("DAY ${d.number}", fontFamily = FontFamily.Cursive, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Ink)
        }
        Spacer(Modifier.width(10.dp))
        Column(Modifier.weight(1f)) {
            Text(d.title, fontFamily = FontFamily.Cursive, fontWeight = FontWeight.Bold, fontSize = 27.sp, color = BlueInk)
            Text(d.topic, fontFamily = FontFamily.Cursive, fontSize = 14.sp, color = Plum)
        }
        Text("${page + 1}/2", fontSize = 11.sp, color = Muted, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun RuledPaper() {
    Canvas(Modifier.fillMaxSize()) {
        var y = 57f
        while (y < size.height) {
            drawLine(Rule, androidx.compose.ui.geometry.Offset(0f, y), androidx.compose.ui.geometry.Offset(size.width, y), 1f)
            y += 28f
        }
        drawLine(Color(0xFFF0A8AF), androidx.compose.ui.geometry.Offset(25f, 0f), androidx.compose.ui.geometry.Offset(25f, size.height), 2f)
    }
}

@Composable
private fun ConceptSketch(day: Int) {
    val labels = when {
        day <= 5 -> listOf("CPU / GPU", "MEMORY", "NETWORK", "STORAGE")
        day <= 10 -> listOf("NODE", "NFS", "S3", "CHECKPOINT")
        day <= 15 -> listOf("NVMe", "RDMA", "IB", "RoCE")
        day <= 25 -> listOf("LINUX", "BASH", "PYTHON", "TOOLS")
        day <= 30 -> listOf("NFS", "S3", "RAID", "I/O")
        else -> listOf("FIO", "LATENCY", "BANDWIDTH", "HPC")
    }

    Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFEAF3FB)), shape = RoundedCornerShape(13.dp)) {
        Column(Modifier.fillMaxWidth().padding(10.dp)) {
            Text("FIELD SKETCH", fontWeight = FontWeight.Black, fontSize = 10.sp, color = Plum, letterSpacing = 1.sp)
            Spacer(Modifier.height(7.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
                labels.forEachIndexed { index, label ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                        Box(
                            Modifier.size(48.dp).background(
                                if (index % 2 == 0) Color(0xFF183E70) else Color(0xFF3E2D63),
                                RoundedCornerShape(11.dp)
                            ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("${index + 1}", color = Color.White, fontWeight = FontWeight.Black, fontSize = 18.sp)
                        }
                        Spacer(Modifier.height(4.dp))
                        Text(label, color = BlueInk, fontFamily = FontFamily.Cursive, fontWeight = FontWeight.Bold, fontSize = 11.sp, textAlign = TextAlign.Center)
                    }
                    if (index < labels.lastIndex) {
                        Text("→", color = Plum, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun CheckpointCard(task: String) {
    Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF0B8)), shape = RoundedCornerShape(12.dp)) {
        Column(Modifier.padding(12.dp)) {
            Text("✓ FIELD CHECK", fontFamily = FontFamily.Cursive, fontWeight = FontWeight.Bold, fontSize = 17.sp, color = Plum)
            Spacer(Modifier.height(4.dp))
            Text(task.replace("**", ""), fontFamily = FontFamily.Cursive, fontSize = 14.sp, lineHeight = 21.sp, color = BlueInk)
        }
    }
}

@Composable
private fun PremiumContent(raw: String, compact: Boolean = false) {
    val lines = raw.replace("\r", "").split("\n")
    Column(verticalArrangement = Arrangement.spacedBy(if (compact) 5.dp else 7.dp)) {
        lines.forEachIndexed { index, original ->
            val line = original.trim()
            if (line.isBlank()) return@forEachIndexed
            val bullet = line.startsWith("•") || line.startsWith("-") || line.startsWith("→")
            val heading = isHeading(line)
            when {
                heading -> {
                    val fill = listOf(Pink, Yellow, Sky, Green)[index.mod(4)]
                    Box(Modifier.background(fill, RoundedCornerShape(6.dp)).padding(horizontal = 9.dp, vertical = 4.dp)) {
                        Text(
                            line.removePrefix("#").trim(),
                            fontFamily = FontFamily.Cursive,
                            fontSize = if (compact) 15.sp else 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Ink
                        )
                    }
                }
                bullet -> Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
                    Text("✦", fontFamily = FontFamily.Cursive, fontWeight = FontWeight.Bold, color = Plum, fontSize = 16.sp, modifier = Modifier.width(20.dp))
                    Text(cleanBullet(line), fontFamily = FontFamily.Cursive, fontSize = if (compact) 14.sp else 15.sp, lineHeight = if (compact) 20.sp else 22.sp, color = BlueInk)
                }
                else -> Text(line.replace("**", ""), fontFamily = FontFamily.Cursive, fontSize = if (compact) 14.sp else 15.sp, lineHeight = if (compact) 21.sp else 23.sp, color = BlueInk)
            }
        }
    }
}

private fun isHeading(s: String): Boolean {
    val letters = s.filter { it.isLetter() }
    val upper = letters.isNotEmpty() && letters.count { it.isUpperCase() }.toFloat() / letters.length > .72f
    val keywords = listOf("KEY IDEA", "CORE IDEA", "REAL WORLD", "WHY IT MATTERS", "HANDS-ON", "INTERVIEW", "TROUBLESHOOTING", "REMEMBER", "VOCABULARY", "ARCHITECTURE", "TOOLS", "CHECKPOINT", "THE BIG PICTURE", "NEXT STEP")
    return s.startsWith("#") || keywords.any { s.uppercase().startsWith(it) } || (upper && s.length < 82)
}

private fun cleanBullet(s: String) = s.removePrefix("•").removePrefix("-").removePrefix("→").trim()
