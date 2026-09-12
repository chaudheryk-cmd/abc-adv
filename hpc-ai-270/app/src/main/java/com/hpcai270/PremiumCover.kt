package com.hpcai270

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Ink = Color(0xFF29252B)
private val Plum = Color(0xFF6B4AA1)
private val Paper = Color(0xFFFFFCF3)

@Composable
fun Cover(day: Int, start: () -> Unit, openIndex: () -> Unit, openBookmark: () -> Unit, hasBookmark: Boolean) {
    Column(
        Modifier.fillMaxSize().background(Color(0xFFF3E9D7)).padding(22.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("FIELD MANUAL", fontSize = 13.sp, fontWeight = FontWeight.Black, color = Plum, letterSpacing = 2.sp)
        Text("HPC + AI", fontSize = 42.sp, fontWeight = FontWeight.Black, color = Ink)
        Text("270 DAYS", fontFamily = FontFamily.Cursive, fontSize = 27.sp, color = Plum)
        Spacer(Modifier.height(20.dp))
        Box(
            Modifier.width(275.dp).height(360.dp).shadow(18.dp, RoundedCornerShape(12.dp))
                .background(Paper, RoundedCornerShape(12.dp))
                .border(1.dp, Color(0xFFD2C1A4), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Column(Modifier.padding(28.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("THE", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Plum)
                Text("HPC + AI", fontFamily = FontFamily.Cursive, fontWeight = FontWeight.Bold, fontSize = 43.sp, color = Ink, textAlign = TextAlign.Center)
                Text("FIELD NOTES", fontFamily = FontFamily.Cursive, fontSize = 27.sp, color = Ink)
                Spacer(Modifier.height(25.dp))
                Text("Linux • Storage • HPC • AI\nNetworks • RDMA • Lustre\nPerformance • Systems", textAlign = TextAlign.Center, fontSize = 13.sp, lineHeight = 21.sp, color = Color(0xFF5F5664))
                Spacer(Modifier.height(25.dp))
                Text("FOUNDATION I  •  DAYS 1–45", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Plum)
            }
        }
        Spacer(Modifier.height(22.dp))
        Button(start, Modifier.fillMaxWidth().height(54.dp), shape = RoundedCornerShape(18.dp)) {
            Text(if (day == 0) "OPEN THE BOOK" else "CONTINUE — DAY ${day + 1}", fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = openIndex, shape = RoundedCornerShape(16.dp)) { Text("☰ INDEX") }
            if (hasBookmark) OutlinedButton(onClick = openBookmark, shape = RoundedCornerShape(16.dp)) { Text("🔖 BOOKMARK") }
        }
        Spacer(Modifier.height(8.dp))
        Text("$day / 270 days completed", fontFamily = FontFamily.Cursive, fontSize = 16.sp, color = Color(0xFF776D79))
    }
}
