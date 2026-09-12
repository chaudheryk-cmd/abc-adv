package com.hpcai270

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val V2Navy = Color(0xFF07111F)
private val V2Navy2 = Color(0xFF0E1C2D)
private val V2White = Color(0xFFF7F9FC)
private val V2Muted = Color(0xFFA8B6C8)
private val V2Purple = Color(0xFF8B5CF6)
private val V2Blue = Color(0xFF43B8FF)
private val V2Cyan = Color(0xFF55E0E8)
private val V2Green = Color(0xFF35D0A0)
private val V2Orange = Color(0xFFFFB454)

private enum class V2Tab { HOME, INDEX, PROGRESS, BOOKMARKS, PROFILE }

@Composable
fun CoverV2(
    completed: Int,
    currentDay: Int,
    currentPage: Int,
    start: () -> Unit,
    openIndex: () -> Unit,
    openBookmark: () -> Unit,
    hasBookmark: Boolean,
    openPage: (Int, Int) -> Unit
) {
    val days = remember { hpcBookDays() }
    var tab by rememberSaveable { mutableStateOf(V2Tab.HOME) }
    var search by rememberSaveable { mutableStateOf(false) }
    var settings by rememberSaveable { mutableStateOf(false) }

    Box(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(V2Navy, Color(0xFF091523), V2Navy2)))) {
        when {
            settings -> SettingsV2 { settings = false }
            search -> SearchV2(days, { search = false }) { d, p -> search = false; openPage(d, p) }
            tab == V2Tab.HOME -> HomeV2(
                completed = completed,
                currentDay = currentDay,
                currentPage = currentPage,
                hasBookmark = hasBookmark,
                start = start,
                openIndex = { tab = V2Tab.INDEX },
                openProgress = { tab = V2Tab.PROGRESS },
                openBookmarks = { tab = V2Tab.BOOKMARKS },
                openProfile = { tab = V2Tab.PROFILE },
                openSearch = { search = true },
                openSettings = { settings = true }
            )
            tab == V2Tab.INDEX -> BookIndex(completed, null, null, openPage) { tab = V2Tab.HOME }
            tab == V2Tab.PROGRESS -> ProgressV2(completed, currentDay, currentPage, days, { tab = V2Tab.HOME }) { tab = V2Tab.INDEX }
            tab == V2Tab.BOOKMARKS -> BookmarkV2(hasBookmark, { tab = V2Tab.HOME }, openBookmark)
            tab == V2Tab.PROFILE -> ProfileV2(completed, { tab = V2Tab.HOME }, { settings = true })
        }

        if (!settings && !search) {
            BottomV2(tab, { tab = V2Tab.HOME }, { tab = V2Tab.INDEX }, { tab = V2Tab.PROGRESS }, { tab = V2Tab.BOOKMARKS }, { tab = V2Tab.PROFILE }, Modifier.align(Alignment.BottomCenter).navigationBarsPadding())
        }
    }
}

@Composable
private fun HomeV2(
    completed: Int,
    currentDay: Int,
    currentPage: Int,
    hasBookmark: Boolean,
    start: () -> Unit,
    openIndex: () -> Unit,
    openProgress: () -> Unit,
    openBookmarks: () -> Unit,
    openProfile: () -> Unit,
    openSearch: () -> Unit,
    openSettings: () -> Unit
) {
    val pct = (completed / 270f).coerceIn(0f, 1f)
    LazyColumn(Modifier.fillMaxSize().padding(horizontal = 18.dp), contentPadding = PaddingValues(top = 16.dp, bottom = 112.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Column { Text("KUNAL'S JOURNEY", color = V2Cyan, fontSize = 12.sp, fontWeight = FontWeight.Black, letterSpacing = 2.5.sp); Text("HPC + AI", color = V2White, fontSize = 23.sp, fontWeight = FontWeight.Black); Text("FIELD MANUAL • REVISED EDITION", color = V2Muted, fontSize = 9.sp, letterSpacing = 1.2.sp) }
                Spacer(Modifier.weight(1f)); HeaderIconV2("⌕", openSearch); HeaderIconV2("☆", openBookmarks); HeaderIconV2("⚙", openSettings)
            }
        }
        item { HeroV2() }
        item {
            Row(Modifier.fillMaxWidth().background(Color(0xCC111F31), RoundedCornerShape(20.dp)).border(1.dp, Color(0xFF29405D), RoundedCornerShape(20.dp)).padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(72.dp).border(5.dp, V2Purple, CircleShape), contentAlignment = Alignment.Center) { Text("${(pct * 100).toInt()}%", color = V2White, fontWeight = FontWeight.Black, fontSize = 16.sp) }
                Spacer(Modifier.width(14.dp))
                Column(Modifier.weight(1f)) {
                    Text("LEARNING PROGRESS", color = V2Muted, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                    Text("$completed / 270 days completed", color = V2White, fontSize = 17.sp, fontWeight = FontWeight.Black)
                    Spacer(Modifier.height(7.dp)); LinearProgressIndicator({ pct }, Modifier.fillMaxWidth().height(7.dp), color = V2Purple, trackColor = Color(0xFF29384B))
                    Text("Resume: Day ${currentDay + 1} • page ${currentPage + 1}", color = V2Muted, fontSize = 10.sp, modifier = Modifier.padding(top = 5.dp))
                }
            }
        }
        item {
            Button(onClick = start, modifier = Modifier.fillMaxWidth().height(60.dp), shape = RoundedCornerShape(19.dp), colors = ButtonDefaults.buttonColors(containerColor = V2Purple)) {
                Text(if (completed >= 270) "↺  REVIEW THE FIELD MANUAL" else "▶  CONTINUE DAY ${currentDay + 1}  •  PAGE ${currentPage + 1}", fontSize = 15.sp, fontWeight = FontWeight.Black)
            }
        }
        item { SectionTitleV2("LEARNING TOOLS", "Built for daily study and production thinking") }
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ToolV2("▤", "INDEX", "270 days", V2Purple, openIndex)
                ToolV2("↗", "PROGRESS", "$completed complete", V2Blue, openProgress)
                ToolV2("☆", "BOOKMARKS", if (hasBookmark) "1 saved" else "Save pages", V2Orange, openBookmarks)
            }
        }
        item { SectionTitleV2("WHAT YOU WILL MASTER", "From enterprise storage to AI factories") }
        item { MasterCardV2("01", "FOUNDATIONS", "Linux • storage • networking • performance", V2Blue) }
        item { MasterCardV2("02", "HPC STORAGE", "Lustre • Storage Scale • Ceph • parallel I/O", V2Purple) }
        item { MasterCardV2("03", "HIGH-SPEED FABRICS", "RDMA • InfiniBand • RoCEv2 • NVMe-oF", V2Cyan) }
        item { MasterCardV2("04", "AI INFRASTRUCTURE", "GPU data paths • GDS • NCCL • Slurm • Kubernetes", V2Green) }
        item { MasterCardV2("05", "ARCHITECTURE", "Cloud • hybrid • reliability • security • cost", V2Orange) }
        item {
            Row(Modifier.fillMaxWidth().background(Color(0xFF101D2E), RoundedCornerShape(18.dp)).border(1.dp, Color(0xFF263A55), RoundedCornerShape(18.dp)).padding(15.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("✓", color = V2Green, fontSize = 28.sp, fontWeight = FontWeight.Black); Spacer(Modifier.width(10.dp)); Column(Modifier.weight(1f)) { Text("REVISED EDITION", color = V2White, fontWeight = FontWeight.Black, fontSize = 13.sp); Text("Resume state, corrected 270-day index, progress tracking, deeper operational focus and production-style architecture thinking.", color = V2Muted, fontSize = 10.sp, lineHeight = 14.sp) }
            }
        }
    }
}

@Composable private fun HeroV2() {
    Box(Modifier.fillMaxWidth().height(164.dp).background(Brush.linearGradient(listOf(Color(0xFF101D34), Color(0xFF142943), Color(0xFF0C1727))), RoundedCornerShape(24.dp)).border(1.dp, Color(0xFF294663), RoundedCornerShape(24.dp)), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("GPU", color = V2Cyan, fontSize = 11.sp, fontWeight = FontWeight.Black, letterSpacing = 3.sp)
            Text("⇄  ⇄  ⇄", color = V2Blue, fontSize = 22.sp, fontWeight = FontWeight.Black)
            Text("STORAGE  →  FABRIC  →  GPU", color = V2White, fontSize = 18.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
            Text("270 DAYS • ARCHITECTURE • LABS • INTERVIEWS", color = V2Muted, fontSize = 9.sp, letterSpacing = 1.2.sp, modifier = Modifier.padding(top = 8.dp))
        }
    }
}

@Composable private fun SectionTitleV2(title: String, subtitle: String) { Column { Text(title, color = V2White, fontSize = 15.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp); Text(subtitle, color = V2Muted, fontSize = 10.sp, modifier = Modifier.padding(top = 2.dp)) } }
@Composable private fun HeaderIconV2(icon: String, onClick: () -> Unit) { IconButton(onClick = onClick) { Text(icon, color = V2White, fontSize = 25.sp) } }
@Composable private fun ToolV2(icon: String, title: String, subtitle: String, accent: Color, onClick: () -> Unit) { Column(Modifier.weight(1f).height(88.dp).clickable(onClick = onClick).background(Color(0x99152235), RoundedCornerShape(15.dp)).border(1.dp, accent.copy(alpha = .55f), RoundedCornerShape(15.dp)).padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) { Text(icon, color = accent, fontSize = 23.sp); Text(title, color = V2White, fontSize = 9.sp, fontWeight = FontWeight.Black); Text(subtitle, color = V2Muted, fontSize = 7.sp, textAlign = TextAlign.Center) } }
@Composable private fun MasterCardV2(number: String, title: String, subtitle: String, accent: Color) { Row(Modifier.fillMaxWidth().background(Color(0xCC111F31), RoundedCornerShape(16.dp)).border(1.dp, Color(0xFF263A55), RoundedCornerShape(16.dp)).padding(13.dp), verticalAlignment = Alignment.CenterVertically) { Text(number, color = accent, fontSize = 15.sp, fontWeight = FontWeight.Black, modifier = Modifier.width(34.dp)); Column { Text(title, color = V2White, fontSize = 12.sp, fontWeight = FontWeight.Black); Text(subtitle, color = V2Muted, fontSize = 10.sp, lineHeight = 14.sp) } } }

@Composable private fun ProgressV2(completed: Int, currentDay: Int, currentPage: Int, days: List<Day>, onBack: () -> Unit, openIndex: () -> Unit) { val pct = (completed / 270f).coerceIn(0f, 1f); Column(Modifier.fillMaxSize().padding(18.dp).padding(bottom = 96.dp)) { PanelHeaderV2("PROGRESS", onBack); Spacer(Modifier.height(20.dp)); Text("$completed / 270", color = V2White, fontSize = 46.sp, fontWeight = FontWeight.Black); Text("days completed", color = V2Muted, fontSize = 14.sp); Spacer(Modifier.height(14.dp)); LinearProgressIndicator({ pct }, Modifier.fillMaxWidth().height(11.dp), color = V2Purple, trackColor = Color(0xFF29384B)); Spacer(Modifier.height(18.dp)); InfoRowV2("Current lesson", "Day ${currentDay + 1}"); InfoRowV2("Current page", "${currentPage + 1} / ${days.getOrNull(currentDay)?.pages?.size ?: 1}"); InfoRowV2("Pages available", days.sumOf { it.pages.size }.toString()); InfoRowV2("Curriculum", "270 days"); Spacer(Modifier.height(18.dp)); Text("Your progress is stored locally and the last reading position is restored when the app opens.", color = V2Muted, fontSize = 12.sp, lineHeight = 17.sp); Spacer(Modifier.height(20.dp)); Button(onClick = openIndex, modifier = Modifier.fillMaxWidth().height(54.dp), shape = RoundedCornerShape(16.dp)) { Text("OPEN 270-DAY INDEX") } } }
@Composable private fun BookmarkV2(hasBookmark: Boolean, onBack: () -> Unit, openBookmark: () -> Unit) { Column(Modifier.fillMaxSize().padding(18.dp).padding(bottom = 96.dp)) { PanelHeaderV2("BOOKMARKS", onBack); Spacer(Modifier.height(20.dp)); if (hasBookmark) { Row(Modifier.fillMaxWidth().background(Color(0xFF111F31), RoundedCornerShape(18.dp)).border(1.dp, Color(0xFF2A3E58), RoundedCornerShape(18.dp)).padding(16.dp), verticalAlignment = Alignment.CenterVertically) { Text("☆", color = V2Orange, fontSize = 34.sp); Spacer(Modifier.width(12.dp)); Column(Modifier.weight(1f)) { Text("SAVED PAGE", color = V2White, fontWeight = FontWeight.Black); Text("Your saved reading position is ready.", color = V2Muted, fontSize = 11.sp) }; Button(onClick = openBookmark, shape = RoundedCornerShape(13.dp)) { Text("OPEN") } } } else { Text("No bookmarks yet", color = V2White, fontSize = 23.sp, fontWeight = FontWeight.Black); Text("Use the star in the reader to save an important page.", color = V2Muted, fontSize = 13.sp, modifier = Modifier.padding(top = 7.dp)) } } }
@Composable private fun ProfileV2(completed: Int, onBack: () -> Unit, settings: () -> Unit) { Column(Modifier.fillMaxSize().padding(18.dp).padding(bottom = 96.dp)) { PanelHeaderV2("PROFILE", onBack); Spacer(Modifier.height(22.dp)); Box(Modifier.size(86.dp).background(Color(0xFF1D3047), CircleShape).border(2.dp, V2Cyan, CircleShape).align(Alignment.CenterHorizontally), contentAlignment = Alignment.Center) { Text("KC", color = V2Cyan, fontSize = 25.sp, fontWeight = FontWeight.Black) }; Spacer(Modifier.height(12.dp)); Text("Kunal's Journey", color = V2White, fontSize = 25.sp, fontWeight = FontWeight.Black, modifier = Modifier.align(Alignment.CenterHorizontally)); Text("HPC + AI Field Manual", color = V2Muted, fontSize = 13.sp, modifier = Modifier.align(Alignment.CenterHorizontally)); Spacer(Modifier.height(24.dp)); InfoRowV2("Progress", "$completed / 270 days"); InfoRowV2("Edition", "Revised professional edition"); InfoRowV2("Focus", "HPC + AI storage infrastructure"); Spacer(Modifier.height(18.dp)); OutlinedButton(onClick = settings, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) { Text("APP SETTINGS") } } }
@Composable private fun SettingsV2(onBack: () -> Unit) { Column(Modifier.fillMaxSize().padding(18.dp)) { PanelHeaderV2("SETTINGS", onBack); Spacer(Modifier.height(18.dp)); SettingCardV2("READING POSITION", "Automatically restored on next launch", "ON"); SettingCardV2("PROGRESS", "Completed days stored locally on this device", "ON"); SettingCardV2("CONTENT", "270-day curriculum • variable-length lessons", "270 DAYS"); SettingCardV2("DESIGN", "Notebook reader • architecture diagrams • hardware references", "REVISED") } }
@Composable private fun SettingCardV2(title: String, subtitle: String, value: String) { Row(Modifier.fillMaxWidth().padding(vertical = 7.dp).background(Color(0xFF111F31), RoundedCornerShape(15.dp)).padding(14.dp), verticalAlignment = Alignment.CenterVertically) { Column(Modifier.weight(1f)) { Text(title, color = V2White, fontWeight = FontWeight.Black, fontSize = 12.sp); Text(subtitle, color = V2Muted, fontSize = 10.sp, lineHeight = 14.sp) }; Text(value, color = V2Cyan, fontWeight = FontWeight.Black, fontSize = 10.sp) } }
@Composable private fun SearchV2(days: List<Day>, close: () -> Unit, openPage: (Int, Int) -> Unit) { var q by rememberSaveable { mutableStateOf("") }; val hits = if (q.isBlank()) emptyList() else days.flatMap { d -> d.pages.mapIndexedNotNull { p, text -> if (text.contains(q, ignoreCase = true) || d.title.contains(q, true) || d.topic.contains(q, true)) Triple(d, p, text) else null } }.take(80); Column(Modifier.fillMaxSize().background(Color(0xFF0A1523)).padding(18.dp)) { PanelHeaderV2("SEARCH", close); Spacer(Modifier.height(12.dp)); OutlinedTextField(value = q, onValueChange = { q = it }, modifier = Modifier.fillMaxWidth(), singleLine = true, label = { Text("Search the entire 270-day manual") }); Spacer(Modifier.height(12.dp)); if (q.isBlank()) Text("Search for Lustre, RDMA, FIO, NVMe-oF, Ceph, Slurm, GDS, Kubernetes, Terraform or any other term.", color = V2Muted, fontSize = 12.sp, lineHeight = 17.sp) else LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) { items(hits) { (d, p, text) -> Card(onClick = { openPage(d.number - 1, p) }, colors = CardDefaults.cardColors(Color(0xFF111F31)), shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth()) { Column(Modifier.padding(13.dp)) { Text("DAY ${d.number} • PAGE ${p + 1}", color = V2Cyan, fontSize = 10.sp, fontWeight = FontWeight.Black); Text(d.title, color = V2White, fontWeight = FontWeight.Bold); Text(text.replace("\n", " ").take(160), color = V2Muted, fontSize = 10.sp, maxLines = 2) } } } } } }
@Composable private fun PanelHeaderV2(title: String, onBack: () -> Unit) { Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) { TextButton(onClick = onBack) { Text("‹  Back", color = V2White) }; Spacer(Modifier.weight(1f)); Text(title, color = V2White, fontSize = 18.sp, fontWeight = FontWeight.Black); Spacer(Modifier.weight(1f)); Spacer(Modifier.width(54.dp)) } }
@Composable private fun InfoRowV2(label: String, value: String) { Row(Modifier.fillMaxWidth().padding(vertical = 7.dp), horizontalArrangement = Arrangement.SpaceBetween) { Text(label, color = V2Muted, fontSize = 12.sp); Text(value, color = V2White, fontSize = 12.sp, fontWeight = FontWeight.Bold) } }
@Composable private fun BottomV2(selected: V2Tab, home: () -> Unit, index: () -> Unit, progress: () -> Unit, bookmarks: () -> Unit, profile: () -> Unit, modifier: Modifier) { Row(modifier.fillMaxWidth().height(78.dp).background(Color(0xF90A1523)).border(1.dp, Color(0xFF26384D)).padding(horizontal = 2.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) { NavV2("⌂", "Home", selected == V2Tab.HOME, home); NavV2("▤", "Index", selected == V2Tab.INDEX, index); NavV2("↗", "Progress", selected == V2Tab.PROGRESS, progress); NavV2("☆", "Saved", selected == V2Tab.BOOKMARKS, bookmarks); NavV2("●", "Profile", selected == V2Tab.PROFILE, profile) } }
@Composable private fun RowScope.NavV2(icon: String, label: String, selected: Boolean, onClick: () -> Unit) { Column(Modifier.weight(1f).fillMaxHeight().clickable(onClick = onClick), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) { Text(icon, color = if (selected) V2Purple else Color(0xFF91A0B3), fontSize = 22.sp, fontWeight = FontWeight.Bold); Text(label, color = if (selected) V2White else Color(0xFF91A0B3), fontSize = 9.sp, fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal) } }
