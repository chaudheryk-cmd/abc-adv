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
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.abs

private val Paper = Color(0xFFFFFCF1)
private val Ink = Color(0xFF25212A)
private val BlueInk = Color(0xFF173F8A)
private val Plum = Color(0xFF673B91)
private val Pink = Color(0xFFF6A7C1)
private val Yellow = Color(0xFFFFE680)
private val Green = Color(0xFFBCE7C5)
private val Sky = Color(0xFFB9DDF7)
private val Orange = Color(0xFFFFC98A)
private val Muted = Color(0xFF756B72)
private val Rule = Color(0xFFD9E4F0)

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
    val pageCount = d?.pages?.size ?: 1
    val safePage = page.coerceIn(0, pageCount - 1)
    var previousPage by remember(di) { mutableIntStateOf(safePage) }
    var dragAmount by remember { mutableFloatStateOf(0f) }
    val forward = safePage >= previousPage
    LaunchedEffect(safePage) { previousPage = safePage }

    Column(
        Modifier
            .fillMaxSize()
            .background(Color(0xFF17212B))
    ) {
        Row(
            Modifier.fillMaxWidth().padding(horizontal = 6.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = close) { Text("‹ Close", color = Color.White, fontFamily = FontFamily.Cursive, fontSize = 17.sp) }
            Spacer(Modifier.weight(1f))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("HPC + AI", color = Color.White, fontWeight = FontWeight.Black, fontSize = 15.sp)
                Text("DAY ${di + 1}  •  ${safePage + 1}/$pageCount", color = Color(0xFFD4DCE5), fontSize = 10.sp)
            }
            Spacer(Modifier.weight(1f))
            TextButton(onClick = openIndex) { Text("INDEX", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp) }
            TextButton(onClick = toggleBookmark) { Text(if (bookmarked) "🔖" else "☆", fontSize = 23.sp) }
        }
        LinearProgressIndicator(
            progress = { ((di + 1).toFloat() / 45f).coerceIn(0f, 1f) },
            modifier = Modifier.fillMaxWidth(),
            color = Color(0xFF9D6CFF),
            trackColor = Color(0xFF35424F)
        )

        Box(
            Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 7.dp, vertical = 7.dp)
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
                            (slideInHorizontally { it } + fadeIn()).togetherWith(slideOutHorizontally { -it / 2 } + fadeOut())
                        } else {
                            (slideInHorizontally { -it } + fadeIn()).togetherWith(slideOutHorizontally { it / 2 } + fadeOut())
                        }
                    },
                    label = "handwrittenPageTurn"
                ) { animatedPage ->
                    NotebookPage(d, animatedPage)
                }
            }
        }

        Row(
            Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 7.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = { setPage((safePage - 1).coerceAtLeast(0)) },
                enabled = safePage > 0,
                shape = RoundedCornerShape(20.dp)
            ) { Text("← Previous") }
            Text("Swipe  ←  →", color = Color(0xFFD4DCE5), fontFamily = FontFamily.Cursive, fontSize = 14.sp)
            if (safePage < pageCount - 1) {
                Button(onClick = { setPage(safePage + 1) }, shape = RoundedCornerShape(20.dp)) { Text("Next →") }
            } else {
                Button(onClick = complete, shape = RoundedCornerShape(20.dp)) { Text("✓ Complete day") }
            }
        }
    }
}

@Composable
private fun NotebookPage(d: Day, page: Int) {
    Card(
        Modifier.fillMaxWidth().fillMaxHeight(.98f).shadow(18.dp, RoundedCornerShape(5.dp)),
        colors = CardDefaults.cardColors(containerColor = Paper),
        shape = RoundedCornerShape(5.dp)
    ) {
        Box(Modifier.fillMaxSize()) {
            RuledPaper()
            Column(
                Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(start = 34.dp, end = 20.dp, top = 16.dp, bottom = 18.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    DaySticker(d.number)
                    Spacer(Modifier.width(10.dp))
                    Column(Modifier.weight(1f)) {
                        Text(d.title, fontFamily = FontFamily.Cursive, fontWeight = FontWeight.Bold, fontSize = 27.sp, color = BlueInk)
                        Text(d.topic, fontFamily = FontFamily.Cursive, fontSize = 14.sp, color = Plum)
                    }
                    Text("${page + 1}/" + d.pages.size, fontSize = 10.sp, color = Muted, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.height(8.dp))
                if (page == 0) {
                    HardwareSketch(d.number)
                    Spacer(Modifier.height(8.dp))
                }
                PremiumContent(d.pages[page])
                if (page == d.pages.lastIndex) {
                    Spacer(Modifier.height(10.dp))
                    CheckpointCard(d.task)
                } else {
                    Spacer(Modifier.height(10.dp))
                    Text("✎  sketch it • explain it • connect it to a real system", fontFamily = FontFamily.Cursive, fontSize = 13.sp, color = Muted)
                }
            }
        }
    }
}

@Composable
private fun RuledPaper() {
    Canvas(Modifier.fillMaxSize()) {
        val spacing = 28f
        var y = 58f
        while (y < size.height) {
            drawLine(Rule, androidx.compose.ui.geometry.Offset(0f, y), androidx.compose.ui.geometry.Offset(size.width, y), 1f)
            y += spacing
        }
        drawLine(Color(0xFFF1A9B0), androidx.compose.ui.geometry.Offset(25f, 0f), androidx.compose.ui.geometry.Offset(25f, size.height), 2f)
    }
}

@Composable
private fun DaySticker(day: Int) {
    val colors = listOf(Yellow, Pink, Sky, Green, Orange)
    Box(
        Modifier.background(colors[(day - 1).mod(colors.size)], RoundedCornerShape(8.dp)).padding(horizontal = 11.dp, vertical = 6.dp)
    ) {
        Text("DAY $day", fontFamily = FontFamily.Cursive, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Ink)
    }
}

@Composable
private fun HardwareSketch(day: Int) {
    val label = when {
        day <= 10 -> "COMPUTE + GPU + NETWORK + STORAGE"
        day <= 15 -> "NVMe • RDMA • InfiniBand • RoCE"
        day <= 25 -> "LINUX • SHELL • SYSTEM TOOLS"
        else -> "STORAGE • I/O • PERFORMANCE • HPC"
    }
    Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFEAF4FB)), shape = RoundedCornerShape(12.dp)) {
        Row(Modifier.fillMaxWidth().padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            MiniServerRack()
            Spacer(Modifier.width(9.dp))
            Column(Modifier.weight(1f)) {
                Text("REAL-WORLD HARDWARE", fontWeight = FontWeight.Black, fontSize = 10.sp, color = Plum)
                Text(label, fontFamily = FontFamily.Cursive, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = BlueInk)
                Text("Think: servers → fabric → storage → GPUs", fontSize = 11.sp, color = Muted)
            }
        }
    }
}

@Composable
private fun MiniServerRack() {
    Column(Modifier.width(70.dp).background(Color(0xFF263238), RoundedCornerShape(5.dp)).padding(5.dp), verticalArrangement = Arrangement.spacedBy(3.dp)) {
        repeat(4) { i ->
            Row(Modifier.fillMaxWidth().height(17.dp).background(Color(0xFF455A64), RoundedCornerShape(2.dp)).padding(3.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(5.dp).background(if (i % 2 == 0) Color(0xFF65D67A) else Color(0xFFFFD166)))
                Spacer(Modifier.width(4.dp))
                repeat(4) { Box(Modifier.size(4.dp).background(Color(0xFFB0BEC5))) }
            }
        }
    }
}

@Composable
private fun CheckpointCard(task: String) {
    Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF0B8)), shape = RoundedCornerShape(10.dp)) {
        Column(Modifier.padding(12.dp)) {
            Text("✓ TODAY'S CHECKPOINT", fontFamily = FontFamily.Cursive, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Plum)
            Spacer(Modifier.height(4.dp))
            PremiumContent(task, compact = true)
        }
    }
}

@Composable
private fun PremiumContent(raw: String, compact: Boolean = false) {
    val lines = raw.replace("\r", "").split("\n")
    Column(verticalArrangement = Arrangement.spacedBy(if (compact) 5.dp else 6.dp)) {
        lines.forEachIndexed { index, original ->
            val line = original.trim()
            if (line.isBlank()) return@forEachIndexed
            val bullet = line.startsWith("•") || line.startsWith("-") || line.startsWith("→")
            val heading = isHeading(line)
            when {
                heading -> {
                    val fill = listOf(Pink, Yellow, Sky, Green)[index.mod(4)]
                    Box(Modifier.background(fill, RoundedCornerShape(5.dp)).padding(horizontal = 8.dp, vertical = 3.dp)) {
                        Text(line.removePrefix("#").trim(), fontFamily = FontFamily.Cursive, fontSize = if (compact) 15.sp else 17.sp, fontWeight = FontWeight.Bold, color = Ink)
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
