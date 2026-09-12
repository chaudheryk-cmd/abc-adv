package com.hpcai270

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Navy = Color(0xFF0B1422)
private val Navy2 = Color(0xFF111F33)
private val White = Color(0xFFF7F9FC)
private val Muted = Color(0xFFAAB8C9)
private val Purple = Color(0xFF8B5CF6)
private val Blue = Color(0xFF43B8FF)
private val Cyan = Color(0xFF55E0E8)

@Composable
fun Cover(day: Int, start: () -> Unit, openIndex: () -> Unit, openBookmark: () -> Unit, hasBookmark: Boolean) {
    Column(
        Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Navy, Color(0xFF08111D), Navy2)))
            .padding(horizontal = 18.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CoverTopBar(openIndex, openBookmark, hasBookmark)
        Spacer(Modifier.height(8.dp))
        Text("KUNAL'S JOURNEY", color = Cyan, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 3.sp)
        Text("HPC + AI", color = White, fontSize = 45.sp, fontWeight = FontWeight.Black, letterSpacing = (-1).sp)
        Text("FIELD MANUAL", color = White, fontSize = 21.sp, fontWeight = FontWeight.Medium, letterSpacing = 5.sp)
        Spacer(Modifier.height(4.dp))
        Text("FROM LINUX & STORAGE → HPC → AI INFRASTRUCTURE", color = Muted, fontSize = 11.sp, textAlign = TextAlign.Center, letterSpacing = .7.sp)
        Spacer(Modifier.height(14.dp))
        HpcHeroGraphic(Modifier.fillMaxWidth().height(195.dp))
        Spacer(Modifier.height(12.dp))
        Text("270 DAYS  •  BUILD THE SKILL", color = Color(0xFFD7C8FF), fontSize = 14.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.5.sp)
        Spacer(Modifier.height(12.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FeatureCard("▣", "DEEP\nEXPLANATIONS", "Concept → production")
            FeatureCard("⌘", "REAL\nCOMMANDS", "Hands-on Linux")
        }
        Spacer(Modifier.height(8.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FeatureCard("▤", "REAL\nHARDWARE", "NVIDIA • DDN • Dell")
            FeatureCard("✦", "INTERVIEW\nPREP", "Think like an engineer")
        }
        Spacer(Modifier.weight(1f))
        Button(
            onClick = start,
            modifier = Modifier.fillMaxWidth().height(58.dp),
            shape = RoundedCornerShape(19.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Purple)
        ) {
            Text(if (day == 0) "START THE JOURNEY  ›" else "CONTINUE  —  DAY ${day + 1}  ›", fontSize = 17.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("$day / 270 completed", color = Muted, fontSize = 13.sp)
            Spacer(Modifier.width(12.dp))
            Box(Modifier.width(125.dp).height(6.dp).background(Color(0xFF2A3748), RoundedCornerShape(8.dp))) {
                Box(Modifier.fillMaxHeight().fillMaxWidth((day / 270f).coerceIn(0f, 1f)).background(Brush.horizontalGradient(listOf(Blue, Purple)), RoundedCornerShape(8.dp)))
            }
        }
        Spacer(Modifier.height(4.dp))
        Text("DISCIPLINE BUILDS FREEDOM", color = Color(0xFF718196), fontSize = 10.sp, letterSpacing = 2.sp)
    }
}

@Composable
private fun CoverTopBar(openIndex: () -> Unit, openBookmark: () -> Unit, hasBookmark: Boolean) {
    Row(Modifier.fillMaxWidth().height(44.dp), verticalAlignment = Alignment.CenterVertically) {
        Text("HPC + AI", color = White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Spacer(Modifier.weight(1f))
        TextButton(onClick = openIndex, contentPadding = PaddingValues(horizontal = 10.dp)) {
            Text("INDEX", color = White, fontWeight = FontWeight.Bold, fontSize = 11.sp)
        }
        if (hasBookmark) {
            IconButton(onClick = openBookmark) { Text("♡", color = White, fontSize = 25.sp) }
        }
    }
}

@Composable
private fun RowScope.FeatureCard(icon: String, title: String, subtitle: String) {
    Column(
        Modifier
            .weight(1f)
            .height(76.dp)
            .background(Color(0xCC172538), RoundedCornerShape(14.dp))
            .border(1.dp, Color(0xFF304157), RoundedCornerShape(14.dp))
            .padding(10.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(icon, color = Blue, fontSize = 21.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.width(8.dp))
            Text(title, color = White, fontSize = 10.sp, lineHeight = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = .5.sp)
        }
        Text(subtitle, color = Muted, fontSize = 9.sp)
    }
}

@Composable
private fun HpcHeroGraphic(modifier: Modifier = Modifier) {
    Box(modifier) {
        Canvas(Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val center = Offset(w / 2f, h / 2f + 4f)
            for (r in listOf(38f, 62f, 88f)) {
                drawCircle(Color(0xFF284B73), r, center, style = Stroke(1.5f))
            }
            for (i in 0 until 12) {
                val a = i * (Math.PI * 2 / 12).toFloat()
                val p = Offset(center.x + kotlin.math.cos(a) * 90f, center.y + kotlin.math.sin(a) * 90f)
                drawCircle(if (i % 2 == 0) Blue else Purple, 4.5f, p)
            }
            val rackLeft = center.x - 68f
            val rackTop = center.y - 45f
            repeat(4) { row ->
                val y = rackTop + row * 25f
                drawRoundRect(
                    color = Color(0xFF1B2A3D),
                    topLeft = Offset(rackLeft, y),
                    size = androidx.compose.ui.geometry.Size(136f, 18f),
                    cornerRadius = CornerRadius(5f, 5f)
                )
                repeat(8) { col ->
                    val x = rackLeft + 12f + col * 15f
                    drawCircle(if ((row + col) % 3 == 0) Cyan else Blue, 2.2f, Offset(x, y + 9f))
                }
            }
            val path = Path().apply {
                moveTo(center.x - 130f, h - 18f)
                cubicTo(center.x - 70f, h - 42f, center.x + 70f, h - 42f, center.x + 130f, h - 18f)
            }
            drawPath(path, Purple, style = Stroke(3f, cap = StrokeCap.Round))
        }
        Text("COMPUTE", Modifier.align(Alignment.TopStart).padding(start = 18.dp, top = 14.dp), color = Muted, fontSize = 9.sp, letterSpacing = 1.5.sp)
        Text("STORAGE", Modifier.align(Alignment.TopEnd).padding(end = 18.dp, top = 14.dp), color = Muted, fontSize = 9.sp, letterSpacing = 1.5.sp)
        Text("NETWORK", Modifier.align(Alignment.BottomCenter).padding(bottom = 2.dp), color = Cyan, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
    }
}
