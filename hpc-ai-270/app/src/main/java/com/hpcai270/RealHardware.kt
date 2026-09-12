package com.hpcai270

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

private data class HardwareReference(val name: String, val imageUrl: String, val source: String)

private fun hardwareFor(day: Int): HardwareReference? = when {
    day in setOf(1,3,15,42,45,152,153,158,159,165,166,255,267,270) -> HardwareReference(
        "NVIDIA DGX B200 — real AI compute reference",
        "https://docs.nvidia.com/dgx/dgxb200-user-guide/_images/dgx-b200-with-bezel.png",
        "NVIDIA DGX B200 User Guide"
    )
    day in setOf(10,35,36,39,135,136,137,138,141,145,146,149,150,151,180,181,220,227,230,231,255,267,270) -> HardwareReference(
        "DDN EXAScaler / HPC storage — real installation reference",
        "https://docs.ncsa.illinois.edu/systems/taiga/en/latest/_images/taiga-cover.jpg",
        "NCSA Taiga documentation"
    )
    day in setOf(28,122,123,127,193,228,230,247,252) -> HardwareReference(
        "Enterprise object-storage reference — S3 data tier",
        "https://docs.ncsa.illinois.edu/systems/taiga/en/latest/_images/taiga-cover.jpg",
        "Real HPC storage environment reference; use the architecture lesson for S3 semantics"
    )
    else -> null
}

@Composable
fun RealHardwareReference(day: Int) {
    val item = hardwareFor(day) ?: return
    Card(colors = CardDefaults.cardColors(containerColor = androidx.compose.ui.graphics.Color(0xFFF0E8D7)), shape = RoundedCornerShape(14.dp)) {
        Column(Modifier.fillMaxWidth().padding(9.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
            Text("REAL HARDWARE / FIELD REFERENCE", fontFamily = Handwritten, fontWeight = FontWeight.Bold, fontSize = 17.sp, color = androidx.compose.ui.graphics.Color(0xFF6B3F8F))
            AsyncImage(
                model = item.imageUrl,
                contentDescription = item.name,
                modifier = Modifier.fillMaxWidth().height(150.dp),
                contentScale = ContentScale.Crop
            )
            Text(item.name, fontFamily = Handwritten, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = androidx.compose.ui.graphics.Color(0xFF173F8A))
            Text("Source: ${item.source}", fontFamily = Handwritten, fontSize = 13.sp, color = androidx.compose.ui.graphics.Color(0xFF716A73))
            AdvancedFlowDiagram(day)
        }
    }
}
