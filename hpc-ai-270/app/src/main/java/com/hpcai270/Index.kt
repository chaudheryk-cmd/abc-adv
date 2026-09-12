package com.hpcai270

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

private val Paper = Color(0xFFFFFCF3)
private val Ink = Color(0xFF29252B)
private val Plum = Color(0xFF6B4AA1)
private val Muted = Color(0xFF776D79)
private val Rule = Color(0xFFE4D7C1)
private val Rail = Color(0xFFD2C5B2)

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
    var showGoToDay by remember { mutableStateOf(false) }
    var dayInput by remember { mutableStateOf("") }
    var jumpError by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    fun jumpToDay(dayNumber: Int) {
        val safeDay = dayNumber.coerceIn(1, 270)
        val index = days.indexOfFirst { it.number == safeDay }
        if (index >= 0) {
            expandedDay = safeDay - 1
            scope.launch { listState.animateScrollToItem(index) }
            showGoToDay = false
            jumpError = false
        } else {
            jumpError = true
        }
    }

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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text("HPC + AI FIELD MANUAL", fontFamily = FontFamily.Cursive, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Plum)
                        Text("270-day professional learning path", fontWeight = FontWeight.SemiBold, color = Ink)
                        Spacer(Modifier.height(4.dp))
                        Text("Open any day, inspect every page, or jump directly to a lesson.", fontSize = 13.sp, color = Muted)
                    }
                    OutlinedButton(onClick = { showGoToDay = true }) {
                        Text("Go to Day")
                    }
                }
                if (bookmarkedDay != null && bookmarkedPage != null) {
                    Spacer(Modifier.height(10.dp))
                    OutlinedButton(onClick = { openPage(bookmarkedDay, bookmarkedPage) }) {
                        Text("🔖  Open bookmark — Day ${bookmarkedDay + 1}, page ${bookmarkedPage + 1}")
                    }
                }
            }
        }

        Box(Modifier.fillMaxSize()) {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize().padding(start = 12.dp, end = 22.dp, top = 8.dp, bottom = 8.dp),
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

            IndexFastScrollRail(
                state = listState,
                itemCount = days.size,
                modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight().width(18.dp).padding(vertical = 10.dp)
            )
        }
    }

    if (showGoToDay) {
        AlertDialog(
            onDismissRequest = { showGoToDay = false; jumpError = false },
            title = { Text("Go to Day") },
            text = {
                Column {
                    Text("Enter a day number from 1 to 270.", color = Muted, fontSize = 13.sp)
                    Spacer(Modifier.height(10.dp))
                    OutlinedTextField(
                        value = dayInput,
                        onValueChange = { value ->
                            dayInput = value.filter { it.isDigit() }.take(3)
                            jumpError = false
                        },
                        label = { Text("Day number") },
                        singleLine = true
                    )
                    if (jumpError) {
                        Spacer(Modifier.height(6.dp))
                        Text("Day not found. Enter 1–270.", color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    val number = dayInput.toIntOrNull()
                    if (number != null && number in 1..270) jumpToDay(number) else jumpError = true
                }) { Text("Go") }
            },
            dismissButton = {
                TextButton(onClick = { showGoToDay = false; jumpError = false }) { Text("Cancel") }
            }
        )
    }
}

@Composable
private fun IndexFastScrollRail(
    state: LazyListState,
    itemCount: Int,
    modifier: Modifier = Modifier
) {
    val layout = state.layoutInfo
    val visibleCount = layout.visibleItemsInfo.size.coerceAtLeast(1)
    val fractionVisible = (visibleCount.toFloat() / itemCount.coerceAtLeast(1)).coerceIn(0.06f, 1f)
    val firstVisible = state.firstVisibleItemIndex
    val maxFirst = (itemCount - visibleCount).coerceAtLeast(1)
    val thumbFraction = (firstVisible.toFloat() / maxFirst).coerceIn(0f, 1f)

    Box(modifier.background(Rail.copy(alpha = 0.55f), RoundedCornerShape(9.dp))) {
        BoxWithConstraints(Modifier.fillMaxSize()) {
            val trackHeight = maxHeight
            val thumbHeight = trackHeight * fractionVisible
            val available = (trackHeight - thumbHeight).coerceAtLeast(0.dp)
            val offset = available * thumbFraction

            Box(
                Modifier
                    .fillMaxWidth()
                    .height(thumbHeight)
                    .offset(y = offset)
                    .padding(horizontal = 2.dp)
                    .background(Plum, RoundedCornerShape(7.dp))
                    .pointerInput(itemCount, visibleCount) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            val trackPx = size.height.toFloat().coerceAtLeast(1f)
                            val deltaFraction = dragAmount.y / trackPx
                            val target = ((state.firstVisibleItemIndex.toFloat() / maxFirst) + deltaFraction)
                                .coerceIn(0f, 1f)
                            val targetIndex = (target * maxFirst).toInt().coerceIn(0, maxFirst)
                            kotlinx.coroutines.GlobalScope.launch {
                                state.scrollToItem(targetIndex)
                            }
                        }
                    }
            )
        }
    }
}
