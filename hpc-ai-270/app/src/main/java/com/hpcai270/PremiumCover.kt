package com.hpcai270

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Navy = Color(0xFF07111F)
private val Navy2 = Color(0xFF101D2E)
private val White = Color(0xFFF7F9FC)
private val Muted = Color(0xFFAAB8C9)
private val Purple = Color(0xFF8B5CF6)
private val Blue = Color(0xFF43B8FF)
private val Cyan = Color(0xFF55E0E8)
private val Green = Color(0xFF35D0A0)
private val Orange = Color(0xFFFFB454)

private enum class HomeTab { HOME, INDEX, PROGRESS, BOOKMARKS, PROFILE }

@Composable
fun Cover(day: Int, start: () -> Unit, openIndex: () -> Unit, openBookmark: () -> Unit, hasBookmark: Boolean, openPage: (Int, Int) -> Unit) {
    val days = remember { hpcBookDays() }
    var tab by remember { mutableStateOf(HomeTab.HOME) }
    var searchOpen by remember { mutableStateOf(false) }
    var settingsOpen by remember { mutableStateOf(false) }

    fun goIndex() { tab = HomeTab.INDEX; searchOpen = false; settingsOpen = false }
    fun goHome() { tab = HomeTab.HOME; searchOpen = false; settingsOpen = false }

    Box(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Navy, Color(0xFF081321), Navy2)))) {
        when {
            settingsOpen -> SettingsPanel(onClose = { settingsOpen = false })
            searchOpen -> SearchPanel(days = days, onClose = { searchOpen = false }, openDay = { d, p -> searchOpen = false; openPage(d, p) })
            tab == HomeTab.HOME -> HomeDashboard(
                completed = day,
                hasBookmark = hasBookmark,
                start = start,
                openIndex = { goIndex() },
                openBookmark = openBookmark,
                openSearch = { searchOpen = true },
                openSettings = { settingsOpen = true },
                openProgress = { tab = HomeTab.PROGRESS },
                openProfile = { tab = HomeTab.PROFILE }
            )
            tab == HomeTab.INDEX -> BookIndex(
                completed = day,
                bookmarkedDay = null,
                bookmarkedPage = null,
                openPage = openPage,
                close = { goHome() }
            )
            tab == HomeTab.PROGRESS -> ProgressPanel(day, days, onBack = { goHome() }, onIndex = { goIndex() })
            tab == HomeTab.BOOKMARKS -> BookmarkPanel(hasBookmark, onBack = { goHome() }, openBookmark = openBookmark)
            tab == HomeTab.PROFILE -> ProfilePanel(day, onBack = { goHome() }, onSettings = { settingsOpen = true })
        }

        if (!settingsOpen && !searchOpen) {
            BottomNavigationBar(
                selected = tab,
                onHome = ::goHome,
                onIndex = ::goIndex,
                onProgress = { tab = HomeTab.PROGRESS },
                onBookmarks = { tab = HomeTab.BOOKMARKS },
                onProfile = { tab = HomeTab.PROFILE }
            )
        }
    }
}

@Composable
private fun HomeDashboard(completed: Int, hasBookmark: Boolean, start: () -> Unit, openIndex: () -> Unit, openBookmark: () -> Unit, openSearch: () -> Unit, openSettings: () -> Unit, openProgress: () -> Unit, openProfile: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(horizontal = 18.dp, vertical = 12.dp)) {
        Row(Modifier.fillMaxWidth().height(48.dp), verticalAlignment = Alignment.CenterVertically) {
            Column {
                Text("HPC + AI", color = White, fontSize = 17.sp, fontWeight = FontWeight.Black)
                Text("FIELD MANUAL", color = Muted, fontSize = 9.sp, letterSpacing = 2.sp)
            }
            Spacer(Modifier.weight(1f))
            HeaderAction("⌕", openSearch)
            HeaderAction("☆", openBookmark, enabled = hasBookmark)
            HeaderAction("⚙", openSettings)
        }
        LazyColumn(Modifier.fillMaxSize().padding(bottom = 72.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            item {
                Spacer(Modifier.height(4.dp))
                Text("KUNAL'S JOURNEY", color = Cyan, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 4.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                Text("HPC + AI", color = White, fontSize = 46.sp, fontWeight = FontWeight.Black, letterSpacing = (-1.5).sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                Text("FIELD MANUAL", color = White, fontSize = 20.sp, fontWeight = FontWeight.Medium, letterSpacing = 5.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                Text("LINUX & STORAGE  →  HPC  →  AI INFRASTRUCTURE", color = Muted, fontSize = 10.sp, letterSpacing = .5.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth().padding(top = 5.dp))
            }
            item { HpcHeroGraphic(Modifier.fillMaxWidth().height(150.dp)) }
            item { ProgressHero(completed, openProgress) }
            item {
                Button(onClick = start, modifier = Modifier.fillMaxWidth().height(58.dp), shape = RoundedCornerShape(19.dp), colors = ButtonDefaults.buttonColors(containerColor = Purple)) {
                    Text(if (completed == 0) "▶   START THE JOURNEY   ›" else "▶   CONTINUE  —  DAY ${completed + 1}   ›", fontSize = 16.sp, fontWeight = FontWeight.Black)
                }
            }
            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    QuickAction("▤", "INDEX", "Browse all days", openIndex)
                    QuickAction("☆", "BOOKMARKS", "Saved pages", openBookmark)
                    QuickAction("⌕", "SEARCH", "Find a topic", openSearch)
                    QuickAction("⚙", "SETTINGS", "App options", openSettings)
                }
            }
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.width(5.dp).height(24.dp).background(Purple, RoundedCornerShape(4.dp)))
                    Spacer(Modifier.width(10.dp))
                    Text("LEARNING MODULES", color = White, fontSize = 15.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
                    Spacer(Modifier.weight(1f))
                    Text("270 days → master HPC + AI", color = Muted, fontSize = 9.sp)
                }
            }
            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ModuleCard("▣", "DEEP\nEXPLANATIONS", "Concept → production", Purple, start)
                    ModuleCard("⌘", "REAL\nCOMMANDS", "Labs & Linux", Blue, start)
                }
            }
            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ModuleCard("▤", "REAL\nHARDWARE", "NVIDIA • DDN • Dell", Green, start)
                    ModuleCard("✦", "INTERVIEW\nPREP", "Scenarios & answers", Orange, start)
                }
            }
            item {
                Row(Modifier.fillMaxWidth().background(Color(0xFF111E31), RoundedCornerShape(18.dp)).border(1.dp, Color(0xFF263A55), RoundedCornerShape(18.dp)).padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("◎", color = Purple, fontSize = 31.sp)
                    Spacer(Modifier.width(10.dp))
                    Column(Modifier.weight(1f)) {
                        Text("DAILY GOAL", color = Color(0xFFC5B1FF), fontWeight = FontWeight.Black, fontSize = 12.sp, letterSpacing = 1.sp)
                        Text("Learn  •  Practice  •  Build  •  Grow", color = Muted, fontSize = 11.sp)
                    }
                    OutlinedButton(onClick = openIndex, shape = RoundedCornerShape(18.dp), contentPadding = PaddingValues(horizontal = 13.dp)) { Text("VIEW PLAN", color = Blue, fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                }
            }
        }
    }
}

@Composable
private fun HeaderAction(icon: String, onClick: () -> Unit, enabled: Boolean = true) {
    IconButton(onClick = onClick, enabled = enabled) { Text(icon, color = if (enabled) White else Color(0xFF405064), fontSize = 28.sp) }
}

@Composable
private fun ProgressHero(completed: Int, onClick: () -> Unit) {
    val fraction = (completed / 270f).coerceIn(0f, 1f)
    Row(Modifier.fillMaxWidth().background(Color(0xCC111F31), RoundedCornerShape(18.dp)).border(1.dp, Color(0xFF29405D), RoundedCornerShape(18.dp)).clickable(onClick = onClick).padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(70.dp).border(5.dp, Purple, RoundedCornerShape(50.dp)), contentAlignment = Alignment.Center) { Text("${(fraction * 100).toInt()}%", color = White, fontWeight = FontWeight.Black, fontSize = 15.sp) }
        Spacer(Modifier.width(14.dp))
        Column(Modifier.weight(1f)) {
            Text("YOUR PROGRESS", color = Muted, fontSize = 11.sp)
            Text("$completed / 270 days completed", color = White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            LinearProgressIndicator({ fraction }, Modifier.fillMaxWidth().height(6.dp), color = Purple, trackColor = Color(0xFF29384B))
        }
    }
}

@Composable
private fun RowScope.QuickAction(icon: String, title: String, subtitle: String, onClick: () -> Unit) {
    Column(Modifier.weight(1f).height(91.dp).clickable(onClick = onClick).background(Color(0x99152235), RoundedCornerShape(14.dp)).border(1.dp, Color(0xFF293E59), RoundedCornerShape(14.dp)).padding(7.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(icon, color = Blue, fontSize = 25.sp)
        Text(title, color = White, fontSize = 9.sp, fontWeight = FontWeight.Black)
        Text(subtitle, color = Muted, fontSize = 7.sp, textAlign = TextAlign.Center)
    }
}

@Composable
private fun RowScope.ModuleCard(icon: String, title: String, subtitle: String, accent: Color, onClick: () -> Unit) {
    Row(Modifier.weight(1f).height(92.dp).clickable(onClick = onClick).background(Color(0xCC111F31), RoundedCornerShape(17.dp)).border(1.dp, accent.copy(alpha = .75f), RoundedCornerShape(17.dp)).padding(11.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(icon, color = accent, fontSize = 29.sp)
        Spacer(Modifier.width(9.dp))
        Column(Modifier.weight(1f)) {
            Text(title, color = White, fontSize = 11.sp, lineHeight = 13.sp, fontWeight = FontWeight.Black)
            Spacer(Modifier.height(5.dp))
            Text(subtitle, color = Muted, fontSize = 9.sp, lineHeight = 12.sp)
        }
        Text("›", color = accent, fontSize = 24.sp)
    }
}

@Composable
private fun BottomNavigationBar(selected: HomeTab, onHome: () -> Unit, onIndex: () -> Unit, onProgress: () -> Unit, onBookmarks: () -> Unit, onProfile: () -> Unit) {
    Row(Modifier.fillMaxWidth().height(67.dp).background(Color(0xF90A1523)).border(1.dp, Color(0xFF26384D)), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
        NavItem("⌂", "Home", selected == HomeTab.HOME, onHome)
        NavItem("▤", "Index", selected == HomeTab.INDEX, onIndex)
        NavItem("↗", "Progress", selected == HomeTab.PROGRESS, onProgress)
        NavItem("☆", "Bookmarks", selected == HomeTab.BOOKMARKS, onBookmarks)
        NavItem("●", "Profile", selected == HomeTab.PROFILE, onProfile)
    }
}

@Composable
private fun NavItem(icon: String, label: String, selected: Boolean, onClick: () -> Unit) {
    Column(Modifier.width(70.dp).fillMaxHeight().clickable(onClick = onClick), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text(icon, color = if (selected) Purple else Color(0xFF91A0B3), fontSize = 23.sp, fontWeight = FontWeight.Bold)
        Text(label, color = if (selected) White else Color(0xFF91A0B3), fontSize = 9.sp, fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal)
    }
}

@Composable
private fun ProgressPanel(completed: Int, days: List<Day>, onBack: () -> Unit, onIndex: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(18.dp)) {
        PanelHeader("YOUR PROGRESS", onBack)
        Spacer(Modifier.height(12.dp))
        Text("$completed / 270", color = White, fontSize = 42.sp, fontWeight = FontWeight.Black)
        Text("days completed", color = Muted, fontSize = 14.sp)
        Spacer(Modifier.height(18.dp))
        LinearProgressIndicator({ (completed / 270f).coerceIn(0f, 1f) }, Modifier.fillMaxWidth().height(10.dp), color = Purple, trackColor = Color(0xFF29384B))
        Spacer(Modifier.height(22.dp))
        StatRow("Published now", "45 days")
        StatRow("Current foundation", if (completed < 45) "Day ${completed + 1}" else "Foundation complete")
        StatRow("Lessons available", "${days.sumOf { it.pages.size }} pages")
        Spacer(Modifier.height(22.dp))
        Button(onClick = onIndex, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) { Text("OPEN LEARNING INDEX") }
    }
}

@Composable
private fun BookmarkPanel(hasBookmark: Boolean, onBack: () -> Unit, openBookmark: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(18.dp)) {
        PanelHeader("BOOKMARKS", onBack)
        Spacer(Modifier.height(18.dp))
        if (hasBookmark) {
            Row(Modifier.fillMaxWidth().background(Color(0xFF111F31), RoundedCornerShape(16.dp)).padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("☆", color = Purple, fontSize = 32.sp)
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text("SAVED LESSON", color = White, fontWeight = FontWeight.Bold)
                    Text("Jump back to your saved page", color = Muted, fontSize = 12.sp)
                }
                Button(onClick = openBookmark, shape = RoundedCornerShape(14.dp)) { Text("OPEN") }
            }
        } else {
            Text("No bookmarks yet.", color = White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Text("Tap the ☆ icon while reading to save a page here.", color = Muted, fontSize = 14.sp)
        }
    }
}

@Composable
private fun ProfilePanel(completed: Int, onBack: () -> Unit, onSettings: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(18.dp)) {
        PanelHeader("PROFILE", onBack)
        Spacer(Modifier.height(20.dp))
        Box(Modifier.size(82.dp).background(Color(0xFF24344A), RoundedCornerShape(50.dp)).align(Alignment.CenterHorizontally), contentAlignment = Alignment.Center) { Text("KC", color = Cyan, fontSize = 25.sp, fontWeight = FontWeight.Black) }
        Spacer(Modifier.height(12.dp))
        Text("Kunal's Journey", color = White, fontSize = 25.sp, fontWeight = FontWeight.Black, modifier = Modifier.align(Alignment.CenterHorizontally))
        Text("HPC + AI Field Manual", color = Muted, modifier = Modifier.align(Alignment.CenterHorizontally))
        Spacer(Modifier.height(25.dp))
        StatRow("Progress", "$completed / 270 days")
        StatRow("Current edition", "Foundation • Days 1–45")
        StatRow("Goal", "HPC + AI Infrastructure")
        Spacer(Modifier.height(20.dp))
        OutlinedButton(onClick = onSettings, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(15.dp)) { Text("OPEN SETTINGS") }
    }
}

@Composable
private fun SettingsPanel(onClose: () -> Unit) {
    var largeText by remember { mutableStateOf(false) }
    var showHints by remember { mutableStateOf(true) }
    LazyColumn(Modifier.fillMaxSize().padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp), contentPadding = PaddingValues(bottom = 24.dp)) {
        item { PanelHeader("SETTINGS", onClose) }
        item { SettingSwitch("Larger lesson text", "Increase reading size", largeText) { largeText = it } }
        item { SettingSwitch("Swipe hints", "Show navigation guidance", showHints) { showHints = it } }
        item { SettingInfo("BOOK VERSION", "Foundation edition • Days 1–45 currently published") }
        item { SettingInfo("READING MODE", "Handwritten field notes with technical diagrams, commands and real hardware references") }
        item { SettingInfo("PAGE TURN", "Swipe horizontally across a lesson page to turn it") }
    }
}

@Composable
private fun SettingSwitch(title: String, subtitle: String, checked: Boolean, onChange: (Boolean) -> Unit) {
    Row(Modifier.fillMaxWidth().background(Color(0xFF111F31), RoundedCornerShape(16.dp)).padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
        Column(Modifier.weight(1f)) { Text(title, color = White, fontWeight = FontWeight.Bold); Text(subtitle, color = Muted, fontSize = 11.sp) }
        Switch(checked, onCheckedChange = onChange)
    }
}

@Composable
private fun SettingInfo(title: String, text: String) {
    Column(Modifier.fillMaxWidth().background(Color(0xFF111F31), RoundedCornerShape(16.dp)).padding(14.dp)) {
        Text(title, color = Cyan, fontSize = 10.sp, fontWeight = FontWeight.Black, letterSpacing = 1.5.sp)
        Spacer(Modifier.height(4.dp))
        Text(text, color = Muted, fontSize = 12.sp, lineHeight = 17.sp)
    }
}

@Composable
private fun SearchPanel(days: List<Day>, onClose: () -> Unit, openDay: (Int, Int) -> Unit) {
    var query by remember { mutableStateOf("") }
    val results = remember(query, days) {
        if (query.isBlank()) days.take(8) else days.filter { d -> (d.title + " " + d.topic + " " + d.pages.joinToString(" ")).contains(query, ignoreCase = true) }.take(20)
    }
    Column(Modifier.fillMaxSize().padding(18.dp)) {
        PanelHeader("SEARCH", onClose)
        Spacer(Modifier.height(12.dp))
        BasicTextField(query, { query = it }, Modifier.fillMaxWidth().background(Color(0xFF111F31), RoundedCornerShape(15.dp)).border(1.dp, Color(0xFF2C4562), RoundedCornerShape(15.dp)).padding(15.dp), textStyle = TextStyle(color = White, fontSize = 16.sp), singleLine = true, decorationBox = { inner -> if (query.isEmpty()) Text("Search Linux, Lustre, RDMA, NVMe...", color = Muted); inner() })
        Spacer(Modifier.height(12.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(results, key = { it.number }) { d ->
                Row(Modifier.fillMaxWidth().clickable { openDay(d.number - 1, 0) }.background(Color(0xFF111F31), RoundedCornerShape(14.dp)).padding(13.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("${d.number}", color = Purple, fontWeight = FontWeight.Black, fontSize = 18.sp, modifier = Modifier.width(35.dp))
                    Column(Modifier.weight(1f)) { Text(d.title, color = White, fontWeight = FontWeight.Bold); Text(d.topic, color = Muted, fontSize = 11.sp) }
                    Text("›", color = Blue, fontSize = 23.sp)
                }
            }
        }
    }
}

@Composable
private fun PanelHeader(title: String, onBack: () -> Unit) {
    Row(Modifier.fillMaxWidth().height(45.dp), verticalAlignment = Alignment.CenterVertically) {
        TextButton(onClick = onBack) { Text("‹  Back", color = White) }
        Spacer(Modifier.weight(1f))
        Text(title, color = White, fontWeight = FontWeight.Black, fontSize = 17.sp, letterSpacing = 1.5.sp)
        Spacer(Modifier.weight(1f))
        Spacer(Modifier.width(72.dp))
    }
}

@Composable
private fun StatRow(label: String, value: String) {
    Row(Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(label, color = Muted, fontSize = 12.sp, modifier = Modifier.weight(1f))
        Text(value, color = White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
    }
}

@Composable
private fun HpcHeroGraphic(modifier: Modifier = Modifier) {
    Box(modifier) {
        CanvasGraphic(modifier = Modifier.fillMaxSize())
        Text("COMPUTE", Modifier.align(Alignment.TopStart).padding(start = 20.dp, top = 8.dp), color = Muted, fontSize = 9.sp, letterSpacing = 1.5.sp)
        Text("STORAGE", Modifier.align(Alignment.TopEnd).padding(end = 20.dp, top = 8.dp), color = Muted, fontSize = 9.sp, letterSpacing = 1.5.sp)
        Text("NETWORK", Modifier.align(Alignment.BottomCenter), color = Cyan, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
    }
}

@Composable
private fun CanvasGraphic(modifier: Modifier) {
    androidx.compose.foundation.Canvas(modifier) {
        val center = androidx.compose.ui.geometry.Offset(size.width / 2f, size.height / 2f)
        for (r in listOf(34f, 57f, 80f)) drawCircle(Color(0xFF284B73), r, center, style = androidx.compose.ui.graphics.drawscope.Stroke(1.4f))
        repeat(12) { i ->
            val a = i * (Math.PI * 2 / 12).toFloat()
            val p = androidx.compose.ui.geometry.Offset(center.x + kotlin.math.cos(a) * 82f, center.y + kotlin.math.sin(a) * 82f)
            drawCircle(if (i % 2 == 0) Blue else Purple, 4f, p)
        }
        val left = center.x - 61f
        val top = center.y - 39f
        repeat(4) { row ->
            val y = top + row * 22f
            drawRoundRect(Color(0xFF1B2A3D), androidx.compose.ui.geometry.Offset(left, y), androidx.compose.ui.geometry.Size(122f, 16f), androidx.compose.ui.geometry.CornerRadius(4f, 4f))
            repeat(7) { col -> drawCircle(Cyan, 2f, androidx.compose.ui.geometry.Offset(left + 12f + col * 16f, y + 8f)) }
        }
    }
}