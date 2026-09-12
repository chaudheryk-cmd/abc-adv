package com.hpcai270

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Paper = Color(0xFFFFFCF3)
private val Ink = Color(0xFF29252B)
private val Plum = Color(0xFF6B4AA1)
private val Muted = Color(0xFF776D79)
private val Rule = Color(0xFFE4D7C1)

@Composable
fun BookIndex(
    completed: Int,
    bookmarkedDay: Int?,
    bookmarkedPage: Int?,
    openPage: (Int, Int) -> Unit,
    close: () -> Unit
) {
    val days = remember { hpcBookDays() }
    var expandedDay by remember { mutableIntStateOf(-1) }

    Column(Modifier.fillMaxSize().background(Color(0xFFF3E9D7))) {
        Row(
            Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = close) { Text("‹ Home") }
            Spacer(Modifier.weight(1f))
            Text("LEARNING INDEX", fontWeight = FontWeight.Black, fontSize = 20.sp, color = Ink)
            Spacer(Modifier.weight(1f))
            Text("$completed / 270", fontFamily = FontFamily.Cursive, color = Muted)
        }

        Card(
            Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
            colors = CardDefaults.cardColors(Paper),
            shape = RoundedCornerShape(18.dp)
        ) {
            Column(Modifier.padding(16.dp)) {
                Text("HPC + AI FIELD MANUAL", fontFamily = FontFamily.Cursive, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Plum)
                Text("270-day professional learning path", fontWeight = FontWeight.SemiBold, color = Ink)
                Spacer(Modifier.height(4.dp))
                Text("Open any day, inspect every page, or jump directly to a lesson.", fontSize = 13.sp, color = Muted)
                if (bookmarkedDay != null && bookmarkedPage != null) {
                    Spacer(Modifier.height(10.dp))
                    OutlinedButton(onClick = { openPage(bookmarkedDay, bookmarkedPage) }) {
                        Text("🔖  Open bookmark — Day ${bookmarkedDay + 1}, page ${bookmarkedPage + 1}")
                    }
                }
            }
        }

        LazyColumn(
            Modifier.fillMaxSize().padding(horizontal = 12.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            items(days, key = { it.number }) { day ->
                val dayIndex = day.number - 1
                val expanded = expandedDay == dayIndex
                Card(
                    Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(Paper),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(Modifier.fillMaxWidth()) {
                        Row(
                            Modifier.fillMaxWidth().padding(13.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(Modifier.size(42.dp).background(Rule, RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
                                Text("${day.number}", fontWeight = FontWeight.Black, color = Plum)
                            }
                            Spacer(Modifier.width(12.dp))
                            Column(Modifier.weight(1f)) {
                                Text(day.title, fontWeight = FontWeight.Bold, color = Ink, maxLines = 1)
                                Text("${day.pages.size} pages  •  ${day.topic}", fontSize = 12.sp, color = Muted, maxLines = 1)
                            }
                            TextButton(onClick = { expandedDay = if (expanded) -1 else dayIndex }) {
                                Text(if (expanded) "Hide" else "Pages")
                            }
                        }
                        if (expanded) {
                            HorizontalDivider(color = Rule)
                            Column(Modifier.padding(10.dp)) {
                                day.pages.forEachIndexed { pageIndex, text ->
                                    val preview = text.replace("\n", " ").trim().take(88)
                                    TextButton(onClick = { openPage(dayIndex, pageIndex) }, modifier = Modifier.fillMaxWidth()) {
                                        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                                            Text("${pageIndex + 1}", fontWeight = FontWeight.Bold, color = Plum, modifier = Modifier.width(30.dp))
                                            Text(preview.ifBlank { "Lesson page — content check required" }, color = Ink, maxLines = 1)
                                            if (bookmarkedDay == dayIndex && bookmarkedPage == pageIndex) Text("  🔖")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
