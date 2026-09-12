package com.hpcai270

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

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
    Canvas(Modifier.fillMaxWidth().height(92.dp)) {
        val step = size.width / (labels.size + 1)
        val y = size.height / 2
        labels.forEachIndexed { i, _ ->
            val x = step * (i + 1)
            drawCircle(Color(0xFFE7D7B7), 25f, Offset(x, y))
            if (i < labels.lastIndex) {
                drawLine(Color(0xFF8A7667), Offset(x + 27, y), Offset(step * (i + 2) - 27, y), 3f)
            }
        }
    }
}
