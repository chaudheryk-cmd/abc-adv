package com.hpcai270

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Paper = Color(0xFFFFFCF3)
private val Ink = Color(0xFF29252B)
private val Plum = Color(0xFF6B4AA1)
private val Muted = Color(0xFF776D79)
private val Rule = Color(0xFFE4D7C1)
private val Note = Color(0xFFF1E5CF)

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
    val all = remember { hpcBookDays() }
    val d = all.getOrNull(di)
    val pageCount = d?.pages?.size ?: 1
    val safePage = page.coerceIn(0, pageCount - 1)
    var previousPage by remember(di) { mutableIntStateOf(safePage) }
    val forward = safePage >= previousPage
    LaunchedEffect(safePage) { previousPage = safePage }

    Column(Modifier.fillMaxSize().background(Color(0xFFF3E9D7)).padding(horizontal = 10.dp)) {
        Row(Modifier.fillMaxWidth().padding(top = 5.dp), verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = close) { Text("‹ Close", color = Plum) }
            Spacer(Modifier.weight(1f))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("DAY ${di + 1} / 45", fontWeight = FontWeight.Black, fontSize = 16.sp, color = Ink)
                Text("FIELD BOOK  •  ${safePage + 1} / $pageCount", fontSize = 11.sp, color = Muted)
            }
            Spacer(Modifier.weight(1f))
            TextButton(onClick = openIndex) { Text("INDEX", fontWeight = FontWeight.Bold, color = Plum) }
            TextButton(onClick = toggleBookmark) { Text(if (bookmarked) "🔖" else "☆", fontSize = 24.sp) }
        }
        LinearProgressIndicator(
            progress = { ((di + 1).toFloat() / 45f).coerceIn(0f, 1f) },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
            color = Plum,
            trackColor = Rule
        )
        Box(Modifier.fillMaxWidth().weight(1f).padding(top = 6.dp), Alignment.Center) {
            if (d == null) {
                Text("This chapter is being prepared.", fontFamily = FontFamily.Cursive, fontSize = 22.sp, textAlign = TextAlign.Center)
            } else {
                AnimatedContent(
                    targetState = safePage,
                    transitionSpec = {
                        if (forward) {
                            (slideInHorizontally { it / 2 } + fadeIn()).togetherWith(slideOutHorizontally { -it / 2 } + fadeOut())
                        } else {
                            (slideInHorizontally { -it / 2 } + fadeIn()).togetherWith(slideOutHorizontally { it / 2 } + fadeOut())
                        }
                    },
                    label = "pageTurn"
                ) { animatedPage -> Page(d, animatedPage) }
            }
        }
        Row(
            Modifier.fillMaxWidth().padding(vertical = 7.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(onClick = { setPage((safePage - 1).coerceAtLeast(0)) }, enabled = safePage > 0, shape = RoundedCornerShape(18.dp)) {
                Text("← Previous")
            }
            Text("${safePage + 1} / $pageCount", fontWeight = FontWeight.Bold, color = Muted)
            if (safePage < pageCount - 1) {
                Button(onClick = { setPage(safePage + 1) }, shape = RoundedCornerShape(18.dp)) { Text("Turn page →") }
            } else {
                Button(onClick = complete, shape = RoundedCornerShape(18.dp)) { Text("✓ Complete day") }
            }
        }
    }
}

@Composable
fun Page(d: Day, page: Int) {
    Card(
        Modifier.fillMaxWidth().fillMaxHeight(.96f).padding(horizontal = 4.dp).shadow(14.dp, RoundedCornerShape(10.dp)),
        colors = CardDefaults.cardColors(containerColor = Paper),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("DAY ${d.number}", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Plum)
                Spacer(Modifier.weight(1f))
                Text("FIELD NOTE ${page + 1}", fontSize = 10.sp, color = Muted, fontWeight = FontWeight.Bold)
            }
            Text(d.title, fontFamily = FontFamily.Cursive, fontWeight = FontWeight.Bold, fontSize = 29.sp, color = Ink)
            Text(d.topic, fontFamily = FontFamily.Cursive, fontSize = 16.sp, color = Muted)
            Spacer(Modifier.height(12.dp))
            if (page == 0) LessonDiagram(d.number)
            Spacer(Modifier.height(12.dp))
            PremiumContent(d.pages[page])
            Spacer(Modifier.height(12.dp))
            if (page == d.pages.lastIndex) {
                Card(colors = CardDefaults.cardColors(containerColor = Note), shape = RoundedCornerShape(14.dp)) {
                    Column(Modifier.padding(14.dp)) {
                        Text("TODAY'S CHECKPOINT", fontWeight = FontWeight.Black, color = Plum, fontSize = 13.sp)
                        Spacer(Modifier.height(4.dp))
                        PremiumContent(d.task, compact = true)
                    }
                }
            } else {
                Text("✎  sketch it  •  explain it aloud  •  connect it to a real system", fontFamily = FontFamily.Cursive, fontSize = 14.sp, color = Muted)
            }
        }
    }
}

@Composable
private fun PremiumContent(raw: String, compact: Boolean = false) {
    val lines = raw.replace("\\r", "").split("\\n")
    Column(verticalArrangement = Arrangement.spacedBy(if (compact) 5.dp else 7.dp)) {
        lines.forEach { original ->
            val line = original.trim()
            if (line.isBlank()) return@forEach
            val bullet = line.startsWith("•") || line.startsWith("-") || line.startsWith("→")
            val heading = isHeading(line)
            when {
                heading -> Text(line.removePrefix("#").trim(), fontFamily = FontFamily.Cursive, fontSize = if (compact) 16.sp else 17.sp, fontWeight = FontWeight.Bold, color = Plum)
                bullet -> Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
                    Text("•", fontWeight = FontWeight.Black, color = Plum, fontSize = 17.sp, modifier = Modifier.width(18.dp))
                    Text(cleanBullet(line), fontSize = if (compact) 14.sp else 15.sp, lineHeight = if (compact) 20.sp else 23.sp, color = Ink)
                }
                else -> Text(inlineBold(line), fontSize = if (compact) 14.sp else 15.sp, lineHeight = if (compact) 20.sp else 23.sp, color = Ink)
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

private fun inlineBold(text: String) = buildAnnotatedString {
    val parts = text.split("**")
    parts.forEachIndexed { i, part -> if (i % 2 == 1) append(part, SpanStyle(fontWeight = FontWeight.Bold)) else append(part) }
}
