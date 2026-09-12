package com.hpcai270

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LessonDiagram(day: Int) {
    val labels = when (day) {
        in 1..5 -> listOf("USER", "SCHEDULER", "COMPUTE", "STORAGE")
        in 6..8 -> listOf("JOB", "MPI / OMP", "CPU / GPU", "RESULT")
        in 9..10 -> listOf("CLIENTS", "PARALLEL FS", "METADATA", "DATA")
        11, 43 -> listOf("CPU", "PCIe", "NVMe", "FLASH")
        12, 44 -> listOf("HOST", "FABRIC", "NVMe TARGET")
        13, 45 -> listOf("APP", "RDMA", "NIC", "NIC", "APP")
        14, 46, 47 -> listOf("GPU NODE", "HCA / NIC", "SWITCH", "HCA / NIC", "GPU NODE")
        in 26..29 -> listOf("APP", "PROTOCOL", "STORAGE", "MEDIA")
        in 30..34 -> listOf("WORKLOAD", "QUEUE", "STORAGE", "MEASURE")
        in 35..42 -> listOf("CLIENTS", "DATA SERVICE", "METADATA", "DATA", "MEDIA")
        48, 49 -> listOf("DATASET", "STORAGE", "NETWORK", "GPU", "CHECKPOINT")
        else -> listOf("LINUX", "NETWORK", "STORAGE", "HPC FS", "AI / GPU")
    }

    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 2.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        labels.forEachIndexed { index, label ->
            Box(
                modifier = Modifier
                    .widthIn(min = 42.dp, max = 82.dp)
                    .background(Color(0xFFE7D7B7), RoundedCornerShape(10.dp))
                    .padding(horizontal = 5.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    label,
                    fontFamily = FontFamily.Cursive,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    textAlign = TextAlign.Center
                )
            }
            if (index < labels.lastIndex) {
                Text("→", fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 2.dp))
            }
        }
    }
}
