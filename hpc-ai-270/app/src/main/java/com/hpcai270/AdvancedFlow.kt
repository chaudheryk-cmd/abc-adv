package com.hpcai270

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val AFInk = Color(0xFF183452)
private val AFBlue = Color(0xFF2F6FAE)
private val AFPurple = Color(0xFF7650A5)
private val AFGreen = Color(0xFF3D866C)
private val AFOrange = Color(0xFFC97832)
private val AFPaper = Color(0xFFF1F6FA)

@Composable
fun AdvancedFlowDiagram(day: Int) {
    val model = when (day) {
        in 101..117 -> FlowModel("PRODUCTION LINUX NODE", listOf("FIRMWARE", "LINUX", "DRIVERS", "NETWORK", "STORAGE", "SCHEDULER"), "A production node is ready only when every dependency is healthy.")
        in 118..134 -> FlowModel("ENTERPRISE STORAGE I/O PATH", listOf("APP", "FILESYSTEM", "BLOCK/API", "NETWORK", "ARRAY/DATA PLATFORM", "MEDIA"), "Follow the request across layers; the first saturated layer is the likely bottleneck.")
        in 135..151 -> FlowModel("PARALLEL FILESYSTEM", listOf("CLIENTS", "METADATA", "DATA TARGETS", "RDMA/FABRIC", "FLASH", "CAPACITY"), "Metadata and bulk data are different workloads and must be engineered separately.")
        in 152..168 -> FlowModel("HIGH-SPEED FABRIC", listOf("GPU/CPU", "PCIe", "HCA/NIC", "LEAF", "SPINE", "STORAGE/GPU"), "Topology, congestion and locality determine real application performance.")
        in 169..185 -> FlowModel("PERFORMANCE EXPERIMENT", listOf("QUESTION", "BASELINE", "WORKLOAD", "MEASURE", "ANALYZE", "DECIDE"), "A benchmark is an experiment with controlled variables and a falsifiable hypothesis.")
        in 186..202 -> FlowModel("DISTRIBUTED STORAGE", listOf("CLIENT", "POOL", "PLACEMENT", "NODES", "RECOVERY", "SERVICE"), "Scale-out storage turns placement and recovery into distributed-system problems.")
        in 203..219 -> FlowModel("AI CLUSTER ORCHESTRATION", listOf("USER", "SCHEDULER", "GPU NODE", "CONTAINER", "STORAGE", "MONITORING"), "Scheduling changes storage concurrency; storage changes GPU productivity.")
        in 220..236 -> FlowModel("AI DATA PIPELINE", listOf("OBJECT", "PREPROCESS", "CACHE", "FAST STORAGE", "GPU", "CHECKPOINT"), "The goal is sustained data delivery to accelerators, not a single impressive storage number.")
        in 237..253 -> FlowModel("HYBRID CLOUD AI", listOf("ON-PREM", "INTERCONNECT", "CLOUD GPU", "FAST FS", "OBJECT", "ARCHIVE"), "Data gravity, transfer time, performance, security and cost determine placement.")
        else -> FlowModel("AI FACTORY ARCHITECTURE", listOf("USERS", "CONTROL", "GPU", "FABRIC", "STORAGE", "OPERATIONS"), "A senior engineer owns the complete platform and its failure modes.")
    }

    Card(colors = CardDefaults.cardColors(containerColor = AFPaper), shape = RoundedCornerShape(16.dp)) {
        Column(Modifier.fillMaxWidth().padding(11.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
            Text(model.title, fontFamily = Handwritten, fontWeight = FontWeight.Black, fontSize = 17.sp, color = AFPurple)
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(3.dp), verticalAlignment = Alignment.CenterVertically) {
                model.nodes.forEachIndexed { index, label ->
                    FlowBox(label, when (index % 4) { 0 -> AFBlue; 1 -> AFPurple; 2 -> AFGreen; else -> AFOrange })
                    if (index < model.nodes.lastIndex) Text("→", fontFamily = Handwritten, fontWeight = FontWeight.Bold, color = AFPurple, fontSize = 16.sp)
                }
            }
            Text(model.caption, fontFamily = Handwritten, fontSize = 14.sp, color = AFInk, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        }
    }
}

private data class FlowModel(val title: String, val nodes: List<String>, val caption: String)

@Composable
private fun FlowBox(label: String, color: Color) {
    Box(Modifier.width(56.dp).height(42.dp).background(color, RoundedCornerShape(9.dp)).padding(3.dp), contentAlignment = Alignment.Center) {
        Text(label, fontFamily = Handwritten, fontWeight = FontWeight.Bold, fontSize = 10.sp, color = Color.White, textAlign = TextAlign.Center)
    }
}
